CREATE OR REPLACE PACKAGE    PKG_DF_FLOW AS 


procedure  sendDocumentToNextStage(p_docId number, p_infoMsg varchar2, p_initialId number );

END PKG_DF_FLOW;
/


CREATE OR REPLACE PACKAGE BODY    PKG_DF_FLOW AS

/*doc statuses consts*/
	Draft NUMBER := 4;
	AtTheApproval NUMBER:=5; 
	Approval NUMBER:=6;
	AtTheSigning NUMBER :=7;
	Signed NUMBER := 8;
	Valid NUMBER := 9;
	Revoked NUMBER :=10;
	Cancelled NUMBER :=11;
  
/*flow process consts*/
  PROCESS_Approval  NUMBER :=1;
  PROCESS_SIGNE NUMBER := 2;

/*######################################################################*/
/*######################################################################*/
/*######################################################################*/


function getCurrentStatus(p_docId number) return DOC_FLOW_STAGE.ID_FLOW_STAGE%TYPE as
  l_currerntStatus  DOC_CARD.ID_STATUS%TYPE;
begin

select id_status l_currerntStatus   into   l_currerntStatus 
 FROM  DOC_CARD 
 WHERE ID_DOC = p_docId;
 
 return  l_currerntStatus;
end;

/*######################################################################*/
/*######################################################################*/
/*######################################################################*/
function getNextPossibleStatus(p_currerntStatus number) return DOC_FLOW_STAGE.ID_FLOW_STAGE%TYPE as  
  l_nextPosibleStatus DOC_CARD.ID_STATUS%TYPE;
begin
 
  
  IF p_currerntStatus  = Draft THEN 
     l_nextPosibleStatus:=AtTheApproval;
  ELSIF    p_currerntStatus  = AtTheApproval THEN 
     l_nextPosibleStatus:=Approval;
   ELSIF    p_currerntStatus  = Approval THEN 
     l_nextPosibleStatus:=AtTheSigning;
    ELSIF    p_currerntStatus  = AtTheSigning THEN 
      l_nextPosibleStatus:=Signed;
    ELSIF    p_currerntStatus  = Signed THEN  
      l_nextPosibleStatus:=Valid;    
  END IF;  
 
 return l_nextPosibleStatus;
end;

/*######################################################################*/
/*######################################################################*/
/*######################################################################*/

procedure  sendDocumentToNextStage(p_docId number, p_infoMsg varchar2, p_initialId number ) as
  l_currerntStatus  DOC_CARD.ID_STATUS%TYPE;
  l_nextPosibleStatus  DOC_CARD.ID_STATUS%TYPE;
  
  l_nextStage DOC_FLOW_STAGE.ID_FLOW_STAGE%TYPE;
begin
 
 l_currerntStatus  := getCurrentStatus(p_docId);
 l_nextPosibleStatus := getNextPossibleStatus(l_currerntStatus );
 

   IF    l_nextPosibleStatus  = AtTheApproval THEN 
     l_nextStage:=PROCESS_Approval;
   ELSIF    l_nextPosibleStatus  = Approval THEN 
     l_nextStage:=PROCESS_SIGNE;
   ELSE  
     l_nextStage := -1;
  END IF;  
  
 IF l_nextStage  > 0 THEN   
     INSERT INTO DF.DOC_FLOW  (  ID_FLOW_STAGE, ID_DOC,DATE_IN,INITIATOR, INFO_MSG )
      VALUES  (l_nextStage,  p_docId,   SYSDATE,    p_initialId, p_infoMsg  );  
  
  ELSE
    Raise_application_error(-20326,'Not available process for document in status = '||l_currerntStatus );
  END IF;
  
end;


END PKG_DF_FLOW;
/
