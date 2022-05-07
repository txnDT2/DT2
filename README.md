# DT2

DT2 (Differentially Testing Database Transaction) is a tool to automatically test transaction implementations in DBMSs.

# Usage
Requirements:
* Java 11 or above
* [Maven](https://maven.apache.org/) (`sudo apt install maven` on Ubuntu)
* Supported DBMSs: MySQL, MariaDB, TiDB

Important Parameters:
* `--host`: The host used to log into all tested DBMSs. Default: `127.0.0.1`
* `--port`: The port used to log into TiDB. Default: `4000`
* `--username`: The username used to log into all tested DBMSs. Default: `"root"`
* `--password`: The password used to log into all tested DBMSs. Default: `"root"`
* `--txn-num`: The maximum number of transactions. Default: `2`
* `--mysql`: Enables or disables to test MySQL. Default: `false`
* `--mysql-port`: The port of tested MySQL. Default: `3306`
* `--mariadb`: Enables or disables to test MariaDB. Default: `false`
* `--mariadb-port`: The port of tested MariaDB. Default: `3306`
* `tidb --oracle DIFF`: Set TiDB as default DBMS and perform differential testing of transactions

```bash
git clone https://github.com/txnDT2/DT2.git
cd DT2
mvn package -DskipTests
cd target
java -jar DT2-*.jar --mysql --mysql-port 3306 --mariadb --mariadb-port 10006 --port 4000 --txn-num 2 tidb --oracle DIFF
```
# Reproducing
Required Parameters:
* `--set-case`: Enables or disables reproduction. Default: `false`
* `--case-file`: The location of test case file. Default: `""`
* `--txn-num`: The number of reproduced transactions. Default: `2`

```bash
java -jar DT2-*.jar --mysql --mysql-port 3306 --mariadb --mariadb-port 10006 --port 4000 --set-case --case-file .\\cases\\test.txt --txn-num 2 tidb --oracle DIFF
```
# Bug List

| DBMS | Bug ID | Link |
| --- | --- | --- |
| MySQL | 104833 | http://bugs.mysql.com/104833 |
| MySQL | 106629 | http://bugs.mysql.com/106629 |
| MySQL | 106655 | http://bugs.mysql.com/106655 |
| MySQL | 107125 | http://bugs.mysql.com/107125 |
| MariaDB | 26643 | https://jira.mariadb.org/browse/MDEV-26643 |
| MariaDB | 27922 | https://jira.mariadb.org/browse/MDEV-27922 |
| MariaDB | 27992 | https://jira.mariadb.org/browse/MDEV-27992 |
| MariaDB | 28027 | https://jira.mariadb.org/browse/MDEV-28027 |
| MariaDB | 28040 | https://jira.mariadb.org/browse/MDEV-28040 |
| MariaDB | 28140 | https://jira.mariadb.org/browse/MDEV-28140 |
| MariaDB | 28142 | https://jira.mariadb.org/browse/MDEV-28142 |
| TiDB | 28092 | https://github.com/pingcap/tidb/issues/28092 |
| TiDB | 28095 | https://github.com/pingcap/tidb/issues/28095 |
| TiDB | 31405 | https://github.com/pingcap/tidb/issues/31405 |
| TiDB | 33315 | https://github.com/pingcap/tidb/issues/33315 |
| TiDB | 34177 | https://github.com/pingcap/tidb/issues/34177 |