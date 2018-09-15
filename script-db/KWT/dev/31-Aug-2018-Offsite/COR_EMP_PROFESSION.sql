CREATE OR REPLACE VIEW JAX_VW_EMPLOYMENT_TYPE
(COMPONENT_DATA_ID,COMPONENT_ID,COMPONENT_CODE,DATA_DESC,LANGUAGE_ID)
 AS SELECT A.Component_Data_Id,A.Component_Id,A.Component_Code,B.Data_Desc,B.Language_Id 
 from Fs_Biz_Component_Data A,Fs_Biz_Component_Data_Desc B
 where A.Component_Data_Id = B.Component_Data_Id And A.Component_Id = 68
 and B.Language_Id = 1
And A.Active = 'Y';

CREATE OR REPLACE VIEW JAX_VW_PROFESSION_LIST
(COMPONENT_DATA_ID,COMPONENT_ID,COMPONENT_CODE,DATA_DESC,LANGUAGE_ID)
 AS SELECT A.Component_Data_Id,A.Component_Id,A.Component_Code,B.Data_Desc,B.Language_Id 
 from Fs_Biz_Component_Data A,Fs_Biz_Component_Data_Desc B
 where A.Component_Data_Id = B.Component_Data_Id And A.Component_Id = 4
 and B.Language_Id = 1
And A.Active = 'Y';