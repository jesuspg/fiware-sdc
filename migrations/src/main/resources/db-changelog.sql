--liquibase formatted sql

--changeset jesuspg:1-1
CREATE TABLE installableinstance_attribute (
  installableinstance_id INT8 NOT NULL,
  attributes_id          INT8 NOT NULL
);


--changeset jesuspg:1-2
CREATE TABLE metadata (
  id          INT8          NOT NULL,
  description VARCHAR(2048),
  key         VARCHAR(256)  NOT NULL,
  v           INT8,
  value       VARCHAR(2048) NOT NULL
);

--changeset jesuspg:1-3
CREATE TABLE product_metadata (
  product_id   INT8 NOT NULL,
  metadatas_id INT8 NOT NULL
);

--changeset jesuspg:1-4
ALTER TABLE metadata ADD CONSTRAINT metadata_pkey PRIMARY KEY (id);

--changeset jesuspg:1-5
ALTER TABLE product_metadata ADD CONSTRAINT fk_product_metadata_product FOREIGN KEY (product_id)
REFERENCES product (id) NOT DEFERRABLE;

--changeset jesuspg:1-6
ALTER TABLE product_metadata ADD CONSTRAINT fk_product_metadata_metadata FOREIGN KEY (metadatas_id)
REFERENCES metadata (id) NOT DEFERRABLE;

--changeset jesuspg:1-7
ALTER TABLE installableinstance_attribute ADD CONSTRAINT fk_installableinstance_attribute_metadata FOREIGN KEY (installableinstance_id)
REFERENCES installableinstance (id) NOT DEFERRABLE;

--changeset jesuspg:1-8
ALTER TABLE installableinstance_attribute ADD CONSTRAINT fk_installableinstance_attribute_attribute FOREIGN KEY (attributes_id)
REFERENCES attribute (id) NOT DEFERRABLE;

--changeset jesuspg:1-9
ALTER TABLE installableinstance_attribute ADD CONSTRAINT installableinstance_attribute_attributes_id_key UNIQUE (attributes_id);

--changeset jesuspg:1-10
ALTER TABLE product_metadata ADD CONSTRAINT product_metadata_metadatas_id_key UNIQUE (metadatas_id);

--changeset jesuspg:1-11
ALTER TABLE environment_productrelease DROP CONSTRAINT fk_env_prodrel_prodrel;

--changeset jesuspg:1-12
ALTER TABLE environment_productrelease DROP CONSTRAINT fk_env_prodrel_environment;

--changeset jesuspg:1-13
ALTER TABLE environmentinstance DROP CONSTRAINT fk_envinstance_env;

--changeset jesuspg:1-14
ALTER TABLE product_attribute DROP CONSTRAINT fk_productattribute_attribute;

--changeset jesuspg:1-15
ALTER TABLE environmentinstance_productinstance DROP CONSTRAINT fk_envinst_prodinst_envinst;

--changeset jesuspg:1-16
ALTER TABLE environmentinstance_productinstance DROP CONSTRAINT fk_envinst_prodinst_prodinst2;

--changeset jesuspg:1-17
ALTER TABLE applicationinstance DROP CONSTRAINT fk_appinstance_installinst;
--changeset jesuspg:1-18
ALTER TABLE applicationinstance DROP CONSTRAINT fk_appinstance_environmentinstance;
--changeset jesuspg:1-19
ALTER TABLE applicationinstance DROP CONSTRAINT fk_appinstance_applicationrelease;

--changeset jesuspg:1-20
ALTER TABLE applicationrelease_applicationrelease DROP CONSTRAINT fk_apprel_apprel_applicationrelease;

--changeset jesuspg:1-21
ALTER TABLE applicationrelease_applicationrelease DROP CONSTRAINT fk_apprel_apprel_applicationrelease2;

--changeset jesuspg:1-22
ALTER TABLE application DROP CONSTRAINT fk_application_product;

--changeset jesuspg:1-23
ALTER TABLE applicationrelease DROP CONSTRAINT fk_applicationrelease_application;

--changeset jesuspg:1-24
ALTER TABLE applicationrelease DROP CONSTRAINT fk_applicationrelease_environment;
--changeset jesuspg:1-25
ALTER TABLE applicationrelease DROP CONSTRAINT fk_applicationrelease_installablerelease;

--changeset jesuspg:1-26
ALTER TABLE installablerelease_attribute DROP CONSTRAINT fk_instrelease_attrib_installrelease;

--changeset jesuspg:1-27
ALTER TABLE installablerelease_attribute DROP CONSTRAINT fk_instrelease_attrib_attribute;

--changeset jesuspg:1-28
ALTER TABLE environment DROP CONSTRAINT environment_name_key;

--changeset jesuspg:1-29
ALTER TABLE environmentinstance_productinstance DROP CONSTRAINT environmentinstance_productinstance_productinstances_id_key;

--changeset jesuspg:1-30
ALTER TABLE installablerelease_attribute DROP CONSTRAINT installablerelease_attribute_privateattributes_id_key;

--changeset jesuspg:1-31
DROP TABLE application;

--changeset jesuspg:1-32
DROP TABLE applicationinstance;

--changeset jesuspg:1-33
DROP TABLE applicationrelease;

--changeset jesuspg:1-34
DROP TABLE applicationrelease_applicationrelease;

--changeset jesuspg:1-35
DROP TABLE environment;

--changeset jesuspg:1-36
DROP TABLE environment_productrelease;

--changeset jesuspg:1-37
DROP TABLE environmentinstance;

--changeset jesuspg:1-38
DROP TABLE environmentinstance_productinstance;

--changeset jesuspg:1-39
DROP TABLE installablerelease_attribute;

--changeset jesuspg:1-40
ALTER TABLE product_attribute DROP COLUMN metadata_id;
