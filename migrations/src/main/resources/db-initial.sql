--liquibase formatted sql

--changeset initial:1
CREATE SEQUENCE hibernate_sequence;

--changeset initial:2
CREATE TABLE application (
  id   INT8 NOT NULL,
  type VARCHAR(255)
);


--changeset initial:3
CREATE TABLE applicationinstance (
  id                     INT8 NOT NULL,
  application_id         INT8 NOT NULL,
  environmentinstance_id INT8
);

--changeset initial:4
CREATE TABLE applicationrelease (
  id             INT8 NOT NULL,
  application_id INT8 NOT NULL,
  environment_id INT8
);

--changeset initial:5
CREATE TABLE applicationrelease_applicationrelease (
  applicationrelease_id  INT8 NOT NULL,
  transitablereleases_id INT8 NOT NULL
);

--changeset initial:6
CREATE TABLE artifact (
  id                 INT8         NOT NULL,
  name               VARCHAR(256) NOT NULL,
  vdc                VARCHAR(256) NOT NULL,
  productinstance_id INT8         NOT NULL

);

--changeset initial:7
CREATE TABLE artifact_attribute (
  artifact_id   INT8 NOT NULL,
  attributes_id INT8 NOT NULL
);

--changeset initial:8
CREATE TABLE attribute (
  id          INT8          NOT NULL,
  description VARCHAR(2048),
  key         VARCHAR(256)  NOT NULL,
  v           INT8,
  value       VARCHAR(2048) NOT NULL
);

--changeset initial:9
CREATE TABLE configuration_properties (
  key       VARCHAR(255) NOT NULL,
  namespace VARCHAR(255) NOT NULL,
  value     VARCHAR(32672)
);

--changeset initial:10
CREATE TABLE environment (
  id          INT8        NOT NULL,
  description VARCHAR(64),
  name        VARCHAR(64) NOT NULL
);

--changeset initial:11
CREATE TABLE environment_productrelease (
  environment_id     INT8 NOT NULL,
  productreleases_id INT8 NOT NULL
);

--changeset initial:12
CREATE TABLE environmentinstance (
  id             INT8 NOT NULL,
  environment_id INT8 NOT NULL
);

--changeset initial:13
CREATE TABLE environmentinstance_productinstance (
  environmentinstance_id INT8 NOT NULL,
  productinstances_id    INT8 NOT NULL
);

--changeset initial:14
CREATE TABLE installableinstance (
  id       INT8 NOT NULL,
  date     TIMESTAMP WITH TIME ZONE,
  name     VARCHAR(255),
  status   VARCHAR(255),
  vdc      VARCHAR(255),
  domain   VARCHAR(255),
  fqn      VARCHAR(255),
  hostname VARCHAR(255),
  ip       VARCHAR(255),
  ostype   VARCHAR(255)
);

--changeset initial:15
CREATE TABLE installablerelease (
  id           INT8 NOT NULL,
  releasenotes VARCHAR(2048),
  version      VARCHAR(128)
);

--changeset initial:16
CREATE TABLE installablerelease_attribute (
  installablerelease_id INT8 NOT NULL,
  privateattributes_id  INT8 NOT NULL

);

--changeset initial:17
CREATE TABLE nodecommand (
  id    INT8         NOT NULL,
  key   VARCHAR(128) NOT NULL,
  v     INT8,
  value VARCHAR(128) NOT NULL,
  os_id INT8
);

--changeset initial:18
CREATE TABLE os (
  id          INT8       NOT NULL,
  description VARCHAR(2048),
  name        VARCHAR(128),
  ostype      VARCHAR(3) NOT NULL,
  v           INT8,
  version     VARCHAR(128)
);

--changeset initial:19
CREATE TABLE product (
  id          INT8         NOT NULL,
  description VARCHAR(2048),
  name        VARCHAR(256) NOT NULL
);

--changeset initial:20
CREATE TABLE product_attribute (
  product_id    INT8 NOT NULL,
  attributes_id INT8 NOT NULL,
  metadata_id   INT8
);

--changeset initial:21
CREATE TABLE productinstance (
  id                INT8 NOT NULL,
  productrelease_id INT8 NOT NULL
);

--changeset initial:22
CREATE TABLE productrelease (
  id         INT8 NOT NULL,
  product_id INT8 NOT NULL
);

--changeset initial:23
CREATE TABLE productrelease_os (
  productrelease_id INT8 NOT NULL,
  supportedooss_id  INT8 NOT NULL
);

--changeset initial:24
CREATE TABLE productrelease_productrelease (
  productrelease_id      INT8 NOT NULL,
  transitablereleases_id INT8 NOT NULL
);

--changeset initial:25
CREATE TABLE task (
  id                      INT8 NOT NULL,
  description             VARCHAR(1024),
  endtime                 TIMESTAMP WITH TIME ZONE,
  majorerrorcode          VARCHAR(1024),
  message                 VARCHAR(1024),
  minorerrorcode          VARCHAR(255),
  venodrspecificerrorcode VARCHAR(255),
  expiretime              INT8,
  owner_href              VARCHAR(255),
  owner_name              VARCHAR(255),
  owner_type              VARCHAR(255),
  result_href             VARCHAR(255),
  result_name             VARCHAR(255),
  result_type             VARCHAR(255),
  starttime               TIMESTAMP WITH TIME ZONE,
  status                  INT4,
  vdc                     VARCHAR(1024)
);

--changeset initial:26
ALTER TABLE application ADD CONSTRAINT application_pkey PRIMARY KEY (id);

--changeset initial:27
ALTER TABLE applicationinstance ADD CONSTRAINT applicationinstance_pkey PRIMARY KEY (id);

--changeset initial:28
ALTER TABLE applicationrelease ADD CONSTRAINT applicationrelease_pkey PRIMARY KEY (id);

--changeset initial:29
ALTER TABLE artifact ADD CONSTRAINT artifact_pkey PRIMARY KEY (id);

--changeset initial:30
ALTER TABLE attribute ADD CONSTRAINT attribute_pkey PRIMARY KEY (id);

--changeset initial:31
ALTER TABLE configuration_properties ADD CONSTRAINT configuration_properties_pkey PRIMARY KEY (key, namespace);

--changeset initial:32
ALTER TABLE environment ADD CONSTRAINT environment_pkey PRIMARY KEY (id);

--changeset initial:33
ALTER TABLE environmentinstance ADD CONSTRAINT environmentinstance_pkey PRIMARY KEY (id);

--changeset initial:34
ALTER TABLE installableinstance ADD CONSTRAINT installableinstance_pkey PRIMARY KEY (id);

--changeset initial:35
ALTER TABLE installablerelease ADD CONSTRAINT installablerelease_pkey PRIMARY KEY (id);

--changeset initial:36
ALTER TABLE nodecommand ADD CONSTRAINT nodecommand_pkey PRIMARY KEY (id);

--changeset initial:37
ALTER TABLE os ADD CONSTRAINT os_pkey PRIMARY KEY (id);

--changeset initial:38
ALTER TABLE product ADD CONSTRAINT product_pkey PRIMARY KEY (id);

--changeset initial:39
ALTER TABLE productinstance ADD CONSTRAINT productinstance_pkey PRIMARY KEY (id);

--changeset initial:40
ALTER TABLE productrelease ADD CONSTRAINT productrelease_pkey PRIMARY KEY (id);

--changeset initial:41
ALTER TABLE task ADD CONSTRAINT task_pkey PRIMARY KEY (id);

--changeset initial:42
ALTER TABLE environment_productrelease ADD CONSTRAINT fk_env_prodrel_prodrel FOREIGN KEY (productreleases_id)
REFERENCES productrelease (id) NOT DEFERRABLE;

--changeset initial:43
ALTER TABLE environment_productrelease ADD CONSTRAINT fk_env_prodrel_environment FOREIGN KEY (environment_id)
REFERENCES environment (id) NOT DEFERRABLE;

--changeset initial:44
ALTER TABLE productrelease_productrelease ADD CONSTRAINT fk_prodrel_prodrel_prodrel FOREIGN KEY (productrelease_id)
REFERENCES productrelease (id) NOT DEFERRABLE;

--changeset initial:45
ALTER TABLE productrelease_productrelease ADD CONSTRAINT fk_prodrel_prodrel_prodrel2 FOREIGN KEY (transitablereleases_id)
REFERENCES productrelease (id) NOT DEFERRABLE;

--changeset initial:46
ALTER TABLE environmentinstance ADD CONSTRAINT fk_envinstance_env FOREIGN KEY (environment_id)
REFERENCES environment (id) NOT DEFERRABLE;

--changeset initial:47
ALTER TABLE productrelease_os ADD CONSTRAINT fk_productrelease_os_os FOREIGN KEY (supportedooss_id)
REFERENCES os (id) NOT DEFERRABLE;

--changeset initial:48
ALTER TABLE productrelease_os ADD CONSTRAINT fk_productrelease_os_prodrel FOREIGN KEY (productrelease_id)
REFERENCES productrelease (id) NOT DEFERRABLE;

--changeset initial:49
ALTER TABLE product_attribute ADD CONSTRAINT fk_productattribute_prod FOREIGN KEY (product_id)
REFERENCES product (id) NOT DEFERRABLE;

--changeset initial:50
ALTER TABLE product_attribute ADD CONSTRAINT fk_productattribute_attribute FOREIGN KEY (metadata_id)
REFERENCES attribute (id) NOT DEFERRABLE;

--changeset initial:51
ALTER TABLE product_attribute ADD CONSTRAINT fk_productattribute_attribute2 FOREIGN KEY (attributes_id)
REFERENCES attribute (id) NOT DEFERRABLE;

--changeset initial:52
ALTER TABLE environmentinstance_productinstance ADD CONSTRAINT fk_envinst_prodinst_envinst FOREIGN KEY (environmentinstance_id)
REFERENCES environmentinstance (id) NOT DEFERRABLE;

--changeset initial:53
ALTER TABLE environmentinstance_productinstance ADD CONSTRAINT fk_envinst_prodinst_prodinst2 FOREIGN KEY (productinstances_id)
REFERENCES productinstance (id) NOT DEFERRABLE;

--changeset initial:54
ALTER TABLE applicationinstance ADD CONSTRAINT fk_appinstance_installinst FOREIGN KEY (id)
REFERENCES installableinstance (id) NOT DEFERRABLE;

--changeset initial:55
ALTER TABLE applicationinstance ADD CONSTRAINT fk_appinstance_environmentinstance FOREIGN KEY (environmentinstance_id)
REFERENCES environmentinstance (id) NOT DEFERRABLE;

--changeset initial:56
ALTER TABLE applicationinstance ADD CONSTRAINT fk_appinstance_applicationrelease FOREIGN KEY (application_id)
REFERENCES applicationrelease (id) NOT DEFERRABLE;

--changeset initial:57
ALTER TABLE applicationrelease_applicationrelease ADD CONSTRAINT fk_apprel_apprel_applicationrelease FOREIGN KEY (applicationrelease_id)
REFERENCES applicationrelease (id) NOT DEFERRABLE;

--changeset initial:58
ALTER TABLE applicationrelease_applicationrelease ADD CONSTRAINT fk_apprel_apprel_applicationrelease2 FOREIGN KEY (transitablereleases_id)
REFERENCES applicationrelease (id) NOT DEFERRABLE;

--changeset initial:59
ALTER TABLE artifact ADD CONSTRAINT fk_artifact_prodinst FOREIGN KEY (productinstance_id)
REFERENCES productinstance (id) NOT DEFERRABLE;

--changeset initial:60
ALTER TABLE application ADD CONSTRAINT fk_application_product FOREIGN KEY (id)
REFERENCES product (id) NOT DEFERRABLE;

--changeset initial:61
ALTER TABLE nodecommand ADD CONSTRAINT fk_nodecommand_os FOREIGN KEY (os_id)
REFERENCES os (id) NOT DEFERRABLE;

--changeset initial:62
ALTER TABLE applicationrelease ADD CONSTRAINT fk_applicationrelease_application FOREIGN KEY (application_id)
REFERENCES application (id) NOT DEFERRABLE;

--changeset initial:63
ALTER TABLE applicationrelease ADD CONSTRAINT fk_applicationrelease_environment FOREIGN KEY (environment_id)
REFERENCES environment (id) NOT DEFERRABLE;

--changeset initial:64
ALTER TABLE applicationrelease ADD CONSTRAINT fk_applicationrelease_installablerelease FOREIGN KEY (id)
REFERENCES installablerelease (id) NOT DEFERRABLE;

--changeset initial:65
ALTER TABLE artifact_attribute ADD CONSTRAINT fk_artif_attrib_attribute FOREIGN KEY (attributes_id)
REFERENCES attribute (id) NOT DEFERRABLE;

--changeset initial:66
ALTER TABLE artifact_attribute ADD CONSTRAINT fk_artif_attrib_artifact FOREIGN KEY (artifact_id)
REFERENCES artifact (id) NOT DEFERRABLE;

--changeset initial:67
ALTER TABLE installablerelease_attribute ADD CONSTRAINT fk_instrelease_attrib_installrelease FOREIGN KEY (installablerelease_id)
REFERENCES installablerelease (id) NOT DEFERRABLE;

--changeset initial:68
ALTER TABLE installablerelease_attribute ADD CONSTRAINT fk_instrelease_attrib_attribute FOREIGN KEY (privateattributes_id)
REFERENCES attribute (id) NOT DEFERRABLE;

--changeset initial:69
ALTER TABLE productinstance ADD CONSTRAINT fk_productinstance_instllinst FOREIGN KEY (id)
REFERENCES installableinstance (id) NOT DEFERRABLE;

--changeset initial:70
ALTER TABLE productinstance ADD CONSTRAINT fk_productinstance_productrelease FOREIGN KEY (productrelease_id)
REFERENCES productrelease (id) NOT DEFERRABLE;

--changeset initial:71
ALTER TABLE productrelease ADD CONSTRAINT fk_productrelease_product FOREIGN KEY (product_id)
REFERENCES product (id) NOT DEFERRABLE;

--changeset initial:72
ALTER TABLE productrelease ADD CONSTRAINT fk_productrelease_installablerelease FOREIGN KEY (id)
REFERENCES installablerelease (id) NOT DEFERRABLE;

--changeset initial:73
ALTER TABLE artifact_attribute ADD CONSTRAINT artifact_attribute_attributes_id_key UNIQUE (attributes_id);

--changeset initial:74
ALTER TABLE artifact ADD CONSTRAINT artifact_name_key UNIQUE (name);

--changeset initial:75
ALTER TABLE environment ADD CONSTRAINT environment_name_key UNIQUE (name);

--changeset initial:76
ALTER TABLE environmentinstance_productinstance ADD CONSTRAINT environmentinstance_productinstance_productinstances_id_key UNIQUE (productinstances_id);

--changeset initial:77
ALTER TABLE installablerelease_attribute ADD CONSTRAINT installablerelease_attribute_privateattributes_id_key UNIQUE (privateattributes_id);

--changeset initial:78
ALTER TABLE os ADD CONSTRAINT os_ostype_key UNIQUE (ostype);

--changeset initial:79
ALTER TABLE product_attribute ADD CONSTRAINT product_attribute_attributes_id_key UNIQUE (attributes_id);

--changeset initial:80
ALTER TABLE product ADD CONSTRAINT product_name_key UNIQUE (name);

