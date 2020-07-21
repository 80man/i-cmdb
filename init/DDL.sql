CREATE TABLE M_META (
ENNAME varchar(32),
CNNAME varchar(32)
);
 CREATE TABLE P_META (
ENNAME varchar(32),
CNNAME varchar(100),
PNAME varchar(32),
PGROUP varchar(100),
PTYPE numeric(1),
DEFVALUE varchar(200),
MATCHRULE numeric(1),
MATCHRULEVALUE varchar(200)
);
CREATE TABLE R_META (
ENNAME varchar(32),
CNNAME varchar(100),
SOURCEMODEL varchar(32),
TARGETMODEL varchar(32)
);
CREATE TABLE C_FIREWALL (
P_OID numeric(20)  not null primary key,
P_SID varchar(32) ,
P_IP  varchar(200)
);
CREATE INDEX C_FIREWALL_IND_P_SID ON C_FIREWALL (P_SID);

CREATE TABLE C_HOST (
P_OID numeric(20)  not null primary key,
P_SID varchar(32) ,
P_CPUBIT  numeric(20),
P_IP  varchar(200),
P_POWER  numeric(20),
P_WEIGHT  numeric(20,2)
);
CREATE INDEX C_HOST_IND_P_SID ON C_HOST (P_SID);

CREATE TABLE C_SWITCH (
P_OID numeric(20)  not null primary key,
P_SID varchar(32) ,
P_IP  varchar(200)
);
CREATE INDEX C_SWITCH_IND_P_SID ON C_SWITCH (P_SID);

CREATE TABLE R_CONNECTION (
R_SID numeric(20),
R_TID numeric(20),
R_NOTE varchar(100)
);
CREATE INDEX R_CONNECTION_IND ON R_CONNECTION (R_SID,R_TID);

