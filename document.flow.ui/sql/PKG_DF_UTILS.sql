CREATE OR REPLACE PACKAGE                                        PKG_DF_UTILS AS 

 
--FUNCTION GetDocCode(in_idCorr number,  in_idEmp number ) return varchar2;

function   GetCurrentDate    return date ;

FUNCTION GetDocCode(in_idDoc number, in_idStatus number) return varchar2;

function GetEmpDepartment (in_idEmp number) return V_ENT_EMPLOYEE%ROWTYPE;

procedure EditCancelChangeDoc (p_IdDoc number,  p_IdCancelDoc number,  p_idChangeDoc number);

 

END PKG_DF_UTILS;
/


CREATE OR REPLACE PACKAGE body                         PKG_DF_UTILS AS 

/*EDocStatus*/
 DRAFT                  CONSTANT NUMBER:=4;
ATTHEAPPROVAL   CONSTANT NUMBER:= 5;
APPROVAL      CONSTANT NUMBER:=6;
ATTHESIGNING  CONSTANT NUMBER:= 7;
SIGNED        CONSTANT NUMBER:= 8;
VALID         CONSTANT NUMBER:=9;
REVOKED       CONSTANT NUMBER:= 10;
CANCELLED     CONSTANT NUMBER:= 11;
  
  /*Document types*/
  
   IN_DOC_CODE CONSTANT   VARCHAR2(4) :='1.1.';
   OUT_DOC_CODE CONSTANT   VARCHAR2(4) :='1.2.';
   INSIDE_DOC_CODE CONSTANT   VARCHAR2(4) :='1.3.';
   
   INSIDE_ORDER_DOC_CODE CONSTANT   VARCHAR2(6) :='1.3.2.';



function   GetCurrentDate    return date IS
begin 
 RETURN SYSDATE;
END;

FUNCTION GetDocRow(in_idDoc number) return V_DOC_CARD%ROWTYPE AS
 toReturn  V_DOC_CARD%ROWTYPE;
BEGIN
 
 SELECT *  INTO  toReturn FROM V_DOC_CARD WHERE ID_DOC= in_idDoc ;
 RETURN toReturn;
 
 EXCEPTION 
  WHEN NO_DATA_FOUND THEN 
  raise_application_error(-20002,'Documen by id='||in_idDoc|| ' NOT FOUND!');         
 
END;



FUNCTION GetCorrespondentTypeRootCode (in_idCorr number)  return varchar2  AS
  l_CorrCode VARCHAR2(10);
begin

  SELECT ROOT_CODE INTO  l_CorrCode FROM V_CORRESPONDENCE_TYPE_TREE_2
   WHERE ID_CORR = in_idCorr;
 
 RETURN  l_CorrCode; 
end;



FUNCTION GetNexDocNum (in_CorrRootCode VARCHAR2, in_idDepOwner number, in_dateIn date)  return NUMBER is
 toReturn number;  
begin    
  
  select count(id_doc) into toReturn from V_DOC_CARD
       where CORR_ROOT_CODE=in_CorrRootCode 
              AND ID_DEP_OWNER =  in_idDepOwner
              AND DATE_IN BETWEEN trunc(in_dateIn,'YYYY') AND last_day(in_dateIn);       
 
 return toReturn +1;
end;

function GetEmpDepartment (in_idEmp number) return V_ENT_EMPLOYEE%ROWTYPE is
 toReturn V_ENT_EMPLOYEE%ROWTYPE;
begin
 
 SELECT * into toReturn FROM DF.V_ENT_EMPLOYEE
  where OFFICER_NUM = in_idEmp;
  
  return toReturn;
end;


function GetDepartmentAbb (id_idDoc number , in_idDep number) return ENT_DEPARTMENTS.ABBREVIATION%TYPE AS
    toReturn ENT_DEPARTMENTS.ABBREVIATION%TYPE;
begin
 
 IF in_idDep IS NULL THEN
    raise_application_error(-20002,'In document id = '||id_idDoc||'  Department OWNER IS NULL ' );      
 END IF;
 
 SELECT   ABBREVIATION  INTO toReturn 
    FROM DF.ENT_DEPARTMENTS 
 WHERE ID_DEP= in_idDep;   
  RETURN  toReturn;
  
  EXCEPTION 
  WHEN NO_DATA_FOUND THEN 
    raise_application_error(-20002,'Department by id='||in_idDep|| ' NOT FOUND!');      
  
end;


function  GetCodeTypeAbbreviation (in_idCorr number ) RETURN VARCHAR2 AS
 toReturn VARCHAR2(5);
begin
  SELECT abbreviation INTO toReturn FROM DOC_DIC_CORR_TYPE  WHERE ID_CORR = in_idCorr;
  RETURN NVL(toReturn , '');
end;


/*
FUNCTION GetDocCode(in_idCorr number, in_idEmp number) return varchar2 is
 toReturn VARCHAR2(20);
 l_CorrTypeAbbreviation VARCHAR2(20);
 l_CorrRootCode VARCHAR2(10);
begin

  l_CorrRootCode := GetCorrespondentTypeRootCode (in_idCorr);

  IF      IN_DOC_CODE LIKE l_CorrRootCode THEN     
  
  -- ****-****
  
   toReturn := to_char(sysdate,'YYYY')||'_'||GetNexDocCode (in_idCorr, in_idEmp);
   
  elsif  OUT_DOC_CODE  like l_CorrRootCode  then  
  
  --****-****'/**-**
  
  toReturn := to_char(sysdate,'YYYY')||'_'||GetNexDocCode (in_idCorr,in_idEmp) ||'/'||GetEmpDepartment (in_idEmp).DEP_CODE;


  elsif  INSIDE_DOC_CODE like l_CorrRootCode  then
  
  --  ***-***-****-***
  
   
   
   l_CorrTypeAbbreviation := GetCodeTypeAbbreviation(in_idCorr);
   
   
   toReturn := l_CorrTypeAbbreviation||'_'||GetEmpDepartment(in_idEmp).DEP_ABBR||'_'||TO_CHAR(SYSDATE,'YYYY')||'_'||GetNexDocCode(in_idCorr,in_idEmp);
   
  END IF;

 return toReturn;
end;
*/

FUNCTION GetDocCode(in_idDoc number, in_idStatus number) return varchar2 AS
 docCard V_DOC_CARD%ROWTYPE;
 
 toReturn VARCHAR2(20) :=NULL;
 l_CorrTypeAbbreviation VARCHAR2(20);
 
begin
 RETURN '';
  docCard := GetDocRow(in_idDoc);
  
  if DOCCARD.DOC_CODE is null then 
   RETURN  DOCCARD.DOC_CODE ;
  END IF;
  
   IF in_idStatus in  (APPROVAL, SIGNED)  AND INSIDE_DOC_CODE LIKE docCard.CORR_ROOT_CODE THEN    
   
      l_CorrTypeAbbreviation := GetCodeTypeAbbreviation(docCard.ID_CORR );   
    
      toReturn := l_CorrTypeAbbreviation||'-'||GetDepartmentAbb(in_idDoc, docCard.ID_DEP_OWNER)    ||'-'||TO_CHAR(docCard.DATE_IN,'YYYY')||'-'||
              GetNexDocNum (docCard.CORR_ROOT_CODE, docCard.ID_DEP_OWNER, docCard.DATE_IN) ;
   
   END IF;
 
 RETURN toReturn ;
END;



/*
* Editing/inserting   Cancel/Change Documents
*/
procedure EditCancelChangeDoc (p_IdDoc number,  p_IdCancelDoc number,  p_idChangeDoc number) as
  
  recCount number;
  l_IsCancelDocExist number;
  l_IsChangeDocExist number;
 
begin 
 
  select count (1) into  l_IsCancelDocExist from DF.DOC_RELATIONS
  where ID_MAIN_DOC = p_IdDoc  AND ID_REL_TYPE = 2;
  
  select count (1) into  l_IsChangeDocExist from DF.DOC_RELATIONS
  where ID_MAIN_DOC = p_IdDoc  AND ID_REL_TYPE = 3;
  
  IF p_IdCancelDoc IS NOT NULL THEN 
  
       IF   l_IsCancelDocExist >0   THEN        
          UPDATE DF.DOC_RELATIONS
            SET ID_CHILD_DOC = p_IdCancelDoc        
           WHERE ID_MAIN_DOC=p_IdDoc
                AND ID_REL_TYPE = 2;
       ELSE 
          INSERT INTO DF.DOC_RELATIONS  (    ID_MAIN_DOC,   ID_CHILD_DOC, DATE_REL, ID_REL_TYPE )
              values ( p_IdDoc,    p_IdCancelDoc,    sysdate, 2 ); 
              
              update doc_card 
                set id_status = cancelled,
                       DATE_EXPIRE= GetCurrentDate()
               WHERE ID_DOC = p_IdCancelDoc;
       END IF;
 
     
  end if;
  
  IF p_idChangeDoc IS NOT NULL  THEN  
  
              IF l_IsChangeDocExist > 0 THEN               
                     UPDATE DF.DOC_RELATIONS
                          SET ID_CHILD_DOC = p_idChangeDoc
                        WHERE ID_MAIN_DOC=p_IdDoc
                              AND ID_REL_TYPE = 3;            
               ELSE 
                      INSERT INTO DF.DOC_RELATIONS  (    ID_MAIN_DOC,   ID_CHILD_DOC, DATE_REL, ID_REL_TYPE )
                        VALUES ( p_IdDoc,    p_idChangeDoc,    SYSDATE, 3 );                                 
                        
                      update doc_card 
                          set ID_STATUS = CANCELLED,
                                 DATE_EXPIRE= GetCurrentDate
                         WHERE ID_DOC = p_idChangeDoc;
              END IF;
  
  END IF;
 
end;


END PKG_DF_UTILS;
/
