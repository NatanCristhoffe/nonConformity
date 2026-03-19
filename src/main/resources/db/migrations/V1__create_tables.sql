-- V1__create_tables.sql

-- COMPANIES
CREATE TABLE companies (
   id CHAR(36) NOT NULL,
   company_name VARCHAR(255) NOT NULL,
   plan_type VARCHAR(50) NOT NULL,
   document VARCHAR(20) NOT NULL UNIQUE,
   type_document VARCHAR(20)   NOT NULL,
   phone VARCHAR(20) NOT NULL UNIQUE,
   email VARCHAR(255) NOT NULL UNIQUE,
   street VARCHAR(255) NOT NULL,
   number_street INT NOT NULL,
   city VARCHAR(100) NOT NULL,
   state VARCHAR(50) NOT NULL,
   is_active BOOLEAN NOT NULL,
   create_at DATETIME NOT NULL,
   update_at DATETIME NOT NULL,
   PRIMARY KEY (id)
);

-- SECTORS
CREATE TABLE sectors (
 id BIGINT NOT NULL AUTO_INCREMENT,
 name VARCHAR(255) NOT NULL,
 description VARCHAR(255),
 active BOOLEAN NOT NULL,
 company_id CHAR(36) NOT NULL,
 PRIMARY KEY (id),
 UNIQUE (name, company_id),
 CONSTRAINT fk_sector_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

-- USERS
CREATE TABLE users (
   id CHAR(36) NOT NULL,
   first_name VARCHAR(255) NOT NULL,
   last_name  VARCHAR(255) NOT NULL,
   email VARCHAR(255) NOT NULL UNIQUE,
   password VARCHAR(255) NOT NULL,
   phone VARCHAR(20) NOT NULL UNIQUE,
   role VARCHAR(50) NOT NULL,
   enabled BOOLEAN NOT NULL,
   created_at  DATETIME NOT NULL,
   update_at DATETIME NOT NULL,
   sector_id BIGINT NOT NULL,
   company_id  CHAR(36) NOT NULL,
   PRIMARY KEY (id),
   CONSTRAINT fk_user_sector  FOREIGN KEY (sector_id)  REFERENCES sectors(id),
   CONSTRAINT fk_user_company FOREIGN KEY (company_id) REFERENCES companies(id)
);

-- NONCONFORMITIES
CREATE TABLE nonconformities (
     id BIGINT NOT NULL AUTO_INCREMENT,
     company_id CHAR(36) NOT NULL,
     title VARCHAR(150) NOT NULL,
     description TEXT  NULL,
     has_accident_risk BOOLEAN,
     priority_level VARCHAR(50),
     disposition_date DATETIME,
     disposition_closed_at DATETIME,
     linked_rnc_id BIGINT,
     url_evidence VARCHAR(500),
     status VARCHAR(50) NOT NULL,
     disposition_owner_id CHAR(36) NOT NULL,
     effectiveness_analyst_id CHAR(36) NOT NULL,
     created_by_user_id CHAR(36) NOT NULL,
     created_at DATETIME NOT NULL,
     source_department_id BIGINT,
     responsible_department_id BIGINT,
     requires_quality_tool   BOOLEAN,
     selected_tool VARCHAR(50),
     root_cause_id BIGINT,
     closed_at DATETIME,
     PRIMARY KEY (id),
     CONSTRAINT fk_nc_company  FOREIGN KEY (company_id) REFERENCES companies(id),
     CONSTRAINT fk_nc_disposition_owner  FOREIGN KEY (disposition_owner_id) REFERENCES users(id),
     CONSTRAINT fk_nc_eff_analyst  FOREIGN KEY (effectiveness_analyst_id)  REFERENCES users(id),
     CONSTRAINT fk_nc_created_by FOREIGN KEY (created_by_user_id) REFERENCES users(id),
     CONSTRAINT fk_nc_linked_rnc FOREIGN KEY (linked_rnc_id) REFERENCES nonconformities(id),
     CONSTRAINT fk_nc_source_dept FOREIGN KEY (source_department_id) REFERENCES sectors(id),
     CONSTRAINT fk_nc_responsible_dept FOREIGN KEY (responsible_department_id) REFERENCES sectors(id)
);

-- NONCONFORMITY LOGS
CREATE TABLE nonconformity_logs (
    id BIGINT NOT NULL AUTO_INCREMENT,
    message VARCHAR(500),
    created_at DATETIME,
    nonconformity_id BIGINT,
    PRIMARY KEY (id),
    CONSTRAINT fk_log_nc FOREIGN KEY (nonconformity_id) REFERENCES nonconformities(id)
);

-- ROOT CAUSE
CREATE TABLE root_cause (
    id BIGINT NOT NULL AUTO_INCREMENT,
    description TEXT NOT NULL,
    user_created_roote_cause_id CHAR(36) NOT NULL,
    nonconformity_id BIGINT NOT NULL UNIQUE,
    PRIMARY KEY (id),
    CONSTRAINT fk_rootcause_user FOREIGN KEY (user_created_roote_cause_id) REFERENCES users(id),
    CONSTRAINT fk_rootcause_nc FOREIGN KEY (nonconformity_id) REFERENCES nonconformities(id)
);

-- Adiciona FK de root_cause na nonconformities (criada depois para evitar dependência circular)
ALTER TABLE nonconformities
    ADD CONSTRAINT fk_nc_root_cause FOREIGN KEY (root_cause_id) REFERENCES root_cause(id);

-- ACTIONS
CREATE TABLE actions (
     id BIGINT NOT NULL AUTO_INCREMENT,
     non_conformity_id BIGINT NOT NULL,
     responsible_user_id CHAR(36),
     title VARCHAR(120) NOT NULL,
     description TEXT NOT NULL,
     action_type VARCHAR(50) NOT NULL,
     status VARCHAR(50) NOT NULL,
     due_date DATETIME NOT NULL,
     evidence_url VARCHAR(500),
     observation TEXT,
     non_execution_reason TEXT,
     completed_at DATETIME,
     created_at DATETIME NOT NULL,
     updated_at DATETIME    NOT NULL,
     finalized_by_user_id CHAR(36),
     PRIMARY KEY (id),
     CONSTRAINT fk_action_nc FOREIGN KEY (non_conformity_id)     REFERENCES nonconformities(id),
     CONSTRAINT fk_action_responsible FOREIGN KEY (responsible_user_id)   REFERENCES users(id),
     CONSTRAINT fk_action_finalized_by FOREIGN KEY (finalized_by_user_id)  REFERENCES users(id)
);

-- EFFECTIVENESS ANALYSIS
CREATE TABLE effectiveness_analysis (
    id BIGINT NOT NULL AUTO_INCREMENT,
    effective BOOLEAN NOT NULL,
    effectiveness_description TEXT NOT NULL,
    analyzed_at DATETIME NOT NULL,
    analyzed_by_id CHAR(36) NOT NULL,
    non_conformity_id BIGINT NOT NULL UNIQUE,
    PRIMARY KEY (id),
    CONSTRAINT fk_ea_user FOREIGN KEY (analyzed_by_id)    REFERENCES users(id),
    CONSTRAINT fk_ea_nc FOREIGN KEY (non_conformity_id) REFERENCES nonconformities(id)
);

-- FIVE WHY TOOL
CREATE TABLE five_why_tool (
   id BIGINT  NOT NULL AUTO_INCREMENT,
   non_conformity_id BIGINT  NOT NULL UNIQUE,
   completed BOOLEAN NOT NULL DEFAULT FALSE,
   PRIMARY KEY (id),
   CONSTRAINT fk_fwt_nc FOREIGN KEY (non_conformity_id) REFERENCES nonconformities(id)
);

-- FIVE WHY
CREATE TABLE five_why (
  id BIGINT NOT NULL AUTO_INCREMENT,
  level INT NOT NULL,
  question VARCHAR(255) NOT NULL,
  answer TEXT,
  five_why_tool_id BIGINT,
  PRIMARY KEY (id),
  CONSTRAINT fk_fw_tool FOREIGN KEY (five_why_tool_id) REFERENCES five_why_tool(id)
);

-- NOTIFICATIONS
CREATE TABLE notification (
  id CHAR(36) NOT NULL,
  user_id CHAR(36) NOT NULL,
  message VARCHAR(500) NOT NULL,
  is_read BOOLEAN NOT NULL,
  read_at DATETIME,
  created_at DATETIME NOT NULL,
  type VARCHAR(50) NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_notification_user ON notification(user_id);
CREATE INDEX idx_notification_read ON notification(is_read);