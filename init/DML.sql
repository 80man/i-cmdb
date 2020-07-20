insert into  M_META values('C_FireWall','防火墙','C_ITDevice' );
insert into  M_META values('C_ITDevice','IT设备',NULL );
insert into  M_META values('C_Host','服务器','C_ITDevice' );
insert into  M_META values('C_Switch','交换机','C_ITDevice' );

insert into  P_META values('cpubit','CPU位数','C_ITDevice','基本信息',2,'64',1,'32|64');
insert into  P_META values('ip','管理IP','C_ITDevice','基本信息',1,NULL,0,NULL);
insert into  P_META values('weight','重量(kg)','C_ITDevice','基本信息',3,NULL,0,NULL);
insert into  P_META values('power','电源功率(W)','C_ITDevice','基本信息',2,NULL,0,NULL);

insert into  R_META values('R_Connection','网络连接','服务器','交换机');
