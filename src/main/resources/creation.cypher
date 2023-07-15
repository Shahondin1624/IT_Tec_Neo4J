//Nodes
LOAD CSV WITH HEADERS FROM 'file:///vbb_neo4j.csv' AS row 
WITH toInteger(row.ID) AS ID, toString(row.StationName) AS StationName, toString(row.District) AS District, toFloat(row.Lat) AS Lat, toFloat(row.Long) AS Long 
MERGE (s:Station {stationID: ID}) SET s.ID = ID, s.StationName = StationName, s.District = District, s.Lat = Lat, s.Long = Long
RETURN ID, StationName, District, Lat, Long;
//Edges
LOAD CSV WITH HEADERS FROM 'file:///vbb_neo4j_edge.csv' AS row
MATCH (n:Station{stationID: toInteger(row.Station1)})
MATCH (n2:Station{stationID: toInteger(row.Station2)}) 
CREATE (n)-[:Type {type: toInteger(row.Type), cost: toInteger(row.Cost)}]->(n2);
