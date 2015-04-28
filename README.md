# Neo4j Performance Test

This code is written to test Neo4j database performance.
 
Please note: Code is not fully complete and so far include the fallowing functionality:<br />
1. Clear Database<br />
2. Fill Database with dummy random data

Not implemented yet:<br />
3. Read/Write Test<br />
4. Read/Write test in distributed mode on Amazon AWS


To run the project:

Build jar:
```
    name@machine:~/<path_to_project>$ sbt assembly
```
 
Run jar:
```
   sudo java -Dhost=<db_host> -Dport=<db_port> -Duser=<db_user> -Dpass=<db_pass> -jar neo4j_stres_test-assembly-1.0.jar [localtest] clear fill
```
    