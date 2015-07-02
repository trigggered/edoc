BEGIN
  DBMS_SCHEDULER.create_job (
    job_name        => 'df.job_docs2Canceled',
    job_type        => 'PLSQL_BLOCK',
    job_action      => 'begin   df.PKG_DF_FLOW.setDocs2CancelledStatusAuto; end;',
    start_date      => SYSTIMESTAMP,
    repeat_interval => 'freq=daily;byhour=01;byminute=0;bysecond=0;',
    end_date        => NULL,
    enabled         => TRUE,
    comments        => 'Job defined setDocs2CancelledStatusAuto.');
END;
;

SELECT * FROM dba_scheduler_jobs;

;

begin
SYS.DBMS_SCHEDULER.DROP_JOB('job_docs2Canceled');
end;


;
