import pymysql
import os
import pandas as pd
import json

class OdcManager:
    def __init__(self):
        self.host = os.getenv('MYSQL_HOST')
        self.port = os.getenv('MYSQL_PORT')
        self.user = os.getenv('MYSQL_USER')
        self.password = os.getenv('MYSQL_PASSWORD')
        self.database = os.getenv('MYSQL_DATABASE')
    def __init__(self, host, port, user, password, database):
        self.host = host
        self.port = port
        self.user = user
        self.password = password
        self.database = database

    def getData(self, experiments, methods=None, instances=None):
        global getData
        queryExperiments = self.constructQuery(str(experiments), (None, str(methods))[methods!=None], (None, str(instances))[instances!=None])
        conn = pymysql.connect(
            host=self.host,
            port=self.port,
            user=self.user,
            passwd=self.password,
            db=self.database)

        with conn:
            with conn.cursor() as cursor:
                sql = "select s.*,  s2.value as 'algorithm_name', i.NAME as 'instance' \
                from STRINGEVENT s left JOIN EXECUTION e  ON s.EXECUTION_ID = e.ID \
                inner JOIN STRINGEVENT s2 ON s.EXECUTION_ID = s2.EXECUTION_ID AND s2.name = 'experimentMethodName' \
                left JOIN INSTANCEDESCRIPTION i ON e.INSTANCE_ID = i.ID \
                left JOIN METHODDESCRIPTION m ON e.METHOD_ID = m.ID, \
                (select s.EXECUTION_ID , max(s.`TIMESTAMP` ) as `TIMESTAMP` from STRINGEVENT s left JOIN EXECUTION e ON s.EXECUTION_ID = e.ID where (" + queryExperiments + ") and name='solution' GROUP by EXECUTION_ID) max_execution \
                WHERE s.EXECUTION_ID = max_execution.EXECUTION_ID AND s.TIMESTAMP = max_execution.TIMESTAMP;"
                dfDataBase = pd.read_sql_query(sql,
                conn)
        experimentsDict = {}
        for index, experiment in dfDataBase.iterrows():
            algorithm_name = experiment['algorithm_name']
            dfExperiment = pd.json_normalize(json.loads(experiment['VALUE']))
            dfExperiment['algorithm_name'] = algorithm_name
            dfExperiment['instance'] = experiment['instance']
            if algorithm_name in experimentsDict:
                experimentsDict[algorithm_name] = pd.concat([experimentsDict[algorithm_name], dfExperiment])
            else:
                experimentsDict[algorithm_name] = dfExperiment
        listExperiments = list(experimentsDict.values())
        return listExperiments
    
    def constructQuery(self,experiments, methods=None, instances=None):
        experiments = experiments.split(",")
        queryExperiments = '(e.EXPERIMENT_ID = '
        for experiment in experiments:
            if(experiment == experiments[-1]):
                queryExperiments += experiment
            else:
                queryExperiments += experiment + " OR e.EXPERIMENT_ID = "
        queryExperiments += ")"
        if(methods!=None):
            methods = methods.split(",")
            queryExperiments += " AND (e.METHOD_ID = "
            for method in methods:
                if(method == methods[-1]):
                    queryExperiments += method
                else:
                    queryExperiments += method + " OR e.METHOD_ID = "
            queryExperiments += ")"
        if(instances!=None):
            instances = instances.split(",")
            queryExperiments += " AND (e.INSTANCE_ID = "
            for instance in instances:
                if(instance == instances[-1]):
                    queryExperiments += instance
                else:
                    queryExperiments += instance + " OR e.INSTANCE_ID = "
            queryExperiments += ")"
        return queryExperiments