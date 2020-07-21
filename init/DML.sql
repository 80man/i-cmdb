insert into  M_META values('C_FireWall','防火墙' );
insert into  M_META values('C_Host','服务器' );
insert into  M_META values('C_Switch','交换机' );

insert into  P_META values('cpubit','CPU位数','C_Host','基本信息',2,'64',1,'32|64');
insert into  P_META values('ip','管理IP','C_Switch','基本信息',1,NULL,0,NULL);
insert into  P_META values('ip','管理IP','C_Host','基本信息',1,NULL,0,NULL);
insert into  P_META values('power','电源功率(W)','C_Host','基本信息',2,NULL,0,NULL);
insert into  P_META values('ip','管理IP','C_FireWall','基本信息',1,NULL,0,NULL);
insert into  P_META values('weight','重量(kg)','C_Host','基本信息',3,NULL,0,NULL);

insert into  R_META values('R_Connection','网络连接','C_Host','C_Switch');
