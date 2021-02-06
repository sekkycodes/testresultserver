CREATE TABLE testsuites (
    id CHAR(36),
    name VARCHAR(255),
    PRIMARY KEY (id)
);


CREATE TABLE testcases(
    id CHAR(36),
    testsuiteId CHAR(36),
    name VARCHAR(255),
    PRIMARY KEY (id)
);

ALTER TABLE testcases ADD CONSTRAINT fk_testcases_testsuites_id
    FOREIGN KEY (testsuiteId) REFERENCES testsuites(id)