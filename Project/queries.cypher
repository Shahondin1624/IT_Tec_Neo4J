//Count all direct neighbors
MATCH (s:Station)-[:Type]->(n) RETURN s.StationName AS `station name`, COUNT(n) AS `neighbors` ORDER BY `neighbors` DESC;
//Show all direct neighbors
MATCH (s:Station)-[:Type]->(n) RETURN s.StationName AS `station name`, COUNT(n) AS `neighbor count`, COLLECT(n.StationName) AS `neighbors`  ORDER BY `neighbor count` DESC;
//Show all neighbors within 5 hops
MATCH (s:Station)-[:Type*1..5]->(n)
RETURN s.StationName AS `station name`, COUNT(DISTINCT n) AS `reachable within 5 hops`, COLLECT(DISTINCT n.StationName) AS `station names`
ORDER BY `reachable within 5 hops` DESC;
//Show route costs
MATCH (s1:Station)-[r:Type]->(s2:Station)
RETURN s1.StationName AS `Station 1`, s2.StationName AS `Station 2`, r.cost AS Cost
ORDER BY r.cost;
