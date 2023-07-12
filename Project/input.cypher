MATCH (n) RETURN DISTINCT n.District AS District;
//7.2.2
MATCH (n) 
WITH n.District as District, count(*) AS Count
RETURN District, Count;
//7.2.3
MATCH (n), (s1)-[r:Type]->(s2)
WITH n.District as District, count(s1) AS Count, r.type AS Type
WHERE Type = 2
RETURN District, Count;
//7.2.4
MATCH  (s1), (s2)
WHERE (s1.District = "Pankow") AND (s2)-[:Type]->(s1)
RETURN COUNT(*) AS Connections;
//7.2.5
MATCH  (s1)-[r:Type]->(s2)
WITH COUNT(r) AS Connections, s1.StationName AS Station
ORDER BY Station
RETURN Station, Connections;
//7.3.1
MATCH  (s1)-[r:Type]->(s2)
WHERE r.cost <=400
RETURN COUNT(r) AS Count;
//7.3.2
MATCH (s1), (s2)
WHERE s1.StationName = "S+U Berlin Hauptbahnhof"
AND s2.StationName = "U Turmstr. (Berlin)"
WITH [(s1)-[r*1..4]->(s2) | r] AS Connections
ORDER BY Connections
RETURN Connections, count(Connections) AS ConnectionCount;
//7.3.3
MATCH  (s1), (s2), (s1)-[*1..1]->(s2)
WHERE s1.StationName = "S+U Berlin Hauptbahnhof"
AND s2.StationName = "U Turmstr. (Berlin)"
WITH (s1)-[*1..1]->(s2) AS Connections
ORDER BY Connections
RETURN Connections, count(Connections) AS ConnectionCount;
//7.3.4
MATCH  (s1), (s2), a=(s1)-[*1..5]->(s2)
WHERE s1.StationName = "S+U Berlin Hauptbahnhof"
AND s2.StationName = "U Viktoria-Luise-Platz (Berlin)"
WITH a AS Connection
ORDER BY Connection ASC
RETURN Connection,
reduce(s = 0, x IN relationships(Connection) | s+ x.cost) AS ConnectionTime;











