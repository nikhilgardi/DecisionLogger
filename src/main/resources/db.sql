/* ================================================================
   DECISION LOGGER - FULL ENTERPRISE DATABASE SETUP
   Author: NikhiTech
   Purpose:
     - Secure authentication
     - Role-Based Access Control (RBAC)
     - Soft delete strategy
     - Universal auditing
     - Auto timestamps
     - Row-Level Security (RLS)
   ================================================================ */


/* ================================================================
   1️⃣ Enable pgcrypto
   Why: Required for bcrypt password hashing using crypt()
   ================================================================ */
CREATE EXTENSION IF NOT EXISTS pgcrypto;



/* ================================================================
   2️⃣ ROLES TABLE (RBAC FOUNDATION)
   ================================================================ */

CREATE TABLE roles (
    id BIGSERIAL PRIMARY KEY,
    role_name VARCHAR(50) UNIQUE NOT NULL,
    description TEXT
);

COMMENT ON TABLE roles IS
'Stores system roles used for Role-Based Access Control (RBAC).';

COMMENT ON COLUMN roles.id IS 'Primary key of roles table.';
COMMENT ON COLUMN roles.role_name IS 'Unique role name (ADMIN, USER).';
COMMENT ON COLUMN roles.description IS 'Description of role permissions.';



/* ================================================================
   Insert Default Roles
   ================================================================ */
INSERT INTO roles (role_name, description)
VALUES
('ADMIN', 'Full system access'),
('USER', 'Standard application user');



/* ================================================================
   3️⃣ USERS TABLE
   ================================================================ */

CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role_id BIGINT REFERENCES roles(id),
    is_active BOOLEAN DEFAULT TRUE,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE users IS
'Stores registered users including authentication and role mapping.';

COMMENT ON COLUMN users.id IS 'Primary key of users table.';
COMMENT ON COLUMN users.full_name IS 'Full name of the user.';
COMMENT ON COLUMN users.email IS 'Unique email used for login.';
COMMENT ON COLUMN users.password_hash IS 'Password hashed using bcrypt via pgcrypto.';
COMMENT ON COLUMN users.role_id IS 'Foreign key referencing roles table.';
COMMENT ON COLUMN users.is_active IS 'Indicates if user account is active.';
COMMENT ON COLUMN users.deleted_at IS 'Soft delete timestamp (NULL = active).';
COMMENT ON COLUMN users.created_at IS 'Timestamp when record was created.';
COMMENT ON COLUMN users.updated_at IS 'Timestamp when record was last updated.';

select * from users

/* ================================================================
   4️⃣ DECISIONS TABLE
   ================================================================ */

CREATE TABLE decisions (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title VARCHAR(200) NOT NULL,
    context TEXT,
    expected_outcome TEXT,
    risk_level VARCHAR(20),
    status VARCHAR(20) DEFAULT 'PENDING',
    actual_outcome TEXT,
    reflection_notes TEXT,
    decision_date DATE DEFAULT CURRENT_DATE,
    deleted_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE decisions IS
'Stores decisions logged by users including expected and actual outcomes.';

COMMENT ON COLUMN decisions.id IS 'Primary key of decisions table.';
COMMENT ON COLUMN decisions.user_id IS 'Foreign key referencing users table.';
COMMENT ON COLUMN decisions.title IS 'Short title of the decision.';
COMMENT ON COLUMN decisions.context IS 'Background reasoning.';
COMMENT ON COLUMN decisions.expected_outcome IS 'Expected result.';
COMMENT ON COLUMN decisions.risk_level IS 'Risk classification.';
COMMENT ON COLUMN decisions.status IS 'Current decision status.';
COMMENT ON COLUMN decisions.actual_outcome IS 'Actual result after execution.';
COMMENT ON COLUMN decisions.reflection_notes IS 'Lessons learned.';
COMMENT ON COLUMN decisions.decision_date IS 'Date when decision was made.';
COMMENT ON COLUMN decisions.deleted_at IS 'Soft delete timestamp.';
COMMENT ON COLUMN decisions.created_at IS 'Creation timestamp.';
COMMENT ON COLUMN decisions.updated_at IS 'Last update timestamp.';



/* ================================================================
   5️⃣ AUTO updated_at TRIGGER
   ================================================================ */

CREATE OR REPLACE FUNCTION set_updated_at()
RETURNS TRIGGER AS
$$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_users_updated
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE FUNCTION set_updated_at();

CREATE TRIGGER trg_decisions_updated
BEFORE UPDATE ON decisions
FOR EACH ROW
EXECUTE FUNCTION set_updated_at();



/* ================================================================
   6️⃣ UNIVERSAL AUDIT TABLE
   ================================================================ */

CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,
    table_name VARCHAR(100),
    operation VARCHAR(20),
    record_id BIGINT,
    old_data JSONB,
    new_data JSONB,
    changed_by BIGINT,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE audit_log IS
'Stores complete audit trail for INSERT, UPDATE, DELETE operations.';

COMMENT ON COLUMN audit_log.table_name IS 'Affected table name.';
COMMENT ON COLUMN audit_log.operation IS 'Operation type.';
COMMENT ON COLUMN audit_log.record_id IS 'Primary key of affected row.';
COMMENT ON COLUMN audit_log.old_data IS 'Row state before modification.';
COMMENT ON COLUMN audit_log.new_data IS 'Row state after modification.';
COMMENT ON COLUMN audit_log.changed_by IS 'User ID performing change.';
COMMENT ON COLUMN audit_log.changed_at IS 'Timestamp of change.';



/* ================================================================
   7️⃣ UNIVERSAL AUDIT TRIGGER
   ================================================================ */

CREATE OR REPLACE FUNCTION universal_audit_trigger()
RETURNS TRIGGER AS
$$
DECLARE
    v_user_id BIGINT;
BEGIN

    v_user_id := current_setting('app.current_user_id', true)::BIGINT;

    IF (TG_OP = 'INSERT') THEN
        INSERT INTO audit_log(table_name, operation, record_id, old_data, new_data, changed_by)
        VALUES (TG_TABLE_NAME, TG_OP, NEW.id, NULL, to_jsonb(NEW), v_user_id);
        RETURN NEW;

    ELSIF (TG_OP = 'UPDATE') THEN
        INSERT INTO audit_log(table_name, operation, record_id, old_data, new_data, changed_by)
        VALUES (TG_TABLE_NAME, TG_OP, NEW.id, to_jsonb(OLD), to_jsonb(NEW), v_user_id);
        RETURN NEW;

    ELSIF (TG_OP = 'DELETE') THEN
        INSERT INTO audit_log(table_name, operation, record_id, old_data, new_data, changed_by)
        VALUES (TG_TABLE_NAME, TG_OP, OLD.id, to_jsonb(OLD), NULL, v_user_id);
        RETURN OLD;
    END IF;

END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_users_audit
AFTER INSERT OR UPDATE OR DELETE ON users
FOR EACH ROW
EXECUTE FUNCTION universal_audit_trigger();

CREATE TRIGGER trg_decisions_audit
AFTER INSERT OR UPDATE OR DELETE ON decisions
FOR EACH ROW
EXECUTE FUNCTION universal_audit_trigger();



/* ================================================================
   8️⃣ CREATE USER FUNCTION
   ================================================================ */

CREATE OR REPLACE FUNCTION create_user(
    p_full_name VARCHAR,
    p_email VARCHAR,
    p_password VARCHAR,
    p_role_name VARCHAR DEFAULT 'USER'
)
RETURNS VOID AS
$$
DECLARE
    v_role_id BIGINT;
BEGIN

    SELECT id INTO v_role_id
    FROM roles
    WHERE role_name = p_role_name;

    INSERT INTO users(full_name, email, password_hash, role_id)
    VALUES (
        p_full_name,
        p_email,
        crypt(p_password, gen_salt('bf')),
        v_role_id
    );

END;
$$ LANGUAGE plpgsql;



/* ================================================================
   9️⃣ CREATE ADMIN FUNCTION
   ================================================================ */

CREATE OR REPLACE FUNCTION create_admin_user()
RETURNS VOID AS
$$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM users WHERE email = 'nikhilgardi@gmail.com') THEN
        PERFORM create_user(
            'Nikhil Gardi',
            'nikhilgardi@gmail.com',
            'Mamta@26101986',
            'ADMIN'
        );
    END IF;
END;
$$ LANGUAGE plpgsql;

select create_admin_user()

/* ================================================================
   🔟 LOGIN VALIDATION FUNCTION
   ================================================================ */

CREATE OR REPLACE FUNCTION validate_login(
    p_email VARCHAR,
    p_password VARCHAR
)
RETURNS TABLE (
    user_id BIGINT,
    full_name VARCHAR,
    role_name VARCHAR
)
AS
$$
BEGIN
    RETURN QUERY
    SELECT u.id, u.full_name, r.role_name
    FROM users u
    JOIN roles r ON u.role_id = r.id
    WHERE u.email = p_email
      AND u.deleted_at IS NULL
      AND u.is_active = TRUE
      AND u.password_hash = crypt(p_password, u.password_hash);
END;
$$ LANGUAGE plpgsql;



/* ================================================================
   1️⃣1️⃣ ROW LEVEL SECURITY
   ================================================================ */

ALTER TABLE decisions ENABLE ROW LEVEL SECURITY;

CREATE POLICY user_select_policy
ON decisions
FOR SELECT
USING (user_id = current_setting('app.current_user_id')::BIGINT);

CREATE POLICY user_update_policy
ON decisions
FOR UPDATE
USING (user_id = current_setting('app.current_user_id')::BIGINT);

CREATE POLICY user_delete_policy
ON decisions
FOR DELETE
USING (user_id = current_setting('app.current_user_id')::BIGINT);

CREATE POLICY admin_override_policy
ON decisions
FOR ALL
USING (
    EXISTS (
        SELECT 1 FROM users u
        JOIN roles r ON u.role_id = r.id
        WHERE u.id = current_setting('app.current_user_id')::BIGINT
        AND r.role_name = 'ADMIN'
    )
);