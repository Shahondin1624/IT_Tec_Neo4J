//1: Count all direct neighbors
MATCH (s:Station)-[:Type]->(n)
RETURN s.StationName AS `station name`, count(n) AS `neighbors`
  ORDER BY `neighbors` DESC;
//2: Show all direct neighbors
MATCH (s:Station)-[:Type]->(n)
RETURN s.StationName AS `station name`, count(n) AS `neighbor count`, collect(n.StationName) AS `neighbors`
  ORDER BY `neighbor count` DESC;
//3: Show all neighbors within 5 hops
MATCH (s:Station)-[:Type*1..5]->(n)
RETURN s.StationName AS `station name`, count(DISTINCT n) AS `reachable within 5 hops`,
       collect(DISTINCT n.StationName) AS `station names`
  ORDER BY `reachable within 5 hops` DESC;
//4: Show route costs
MATCH (s1:Station)-[r:Type]->(s2:Station)
RETURN s1.StationName AS `Station 1`, s2.StationName AS `Station 2`, r.cost AS Cost
  ORDER BY r.cost;
//5: Show all stations next to 'S+U Berlin Hauptbahnhof' when cost < 500
MATCH (s:Station)-[r:Type]->(s1:Station)
  WHERE (s.StationName = 'S+U Berlin Hauptbahnhof') AND (r.cost < 500)
RETURN s.StationName AS `Starting Station`, collect([s1.StationName, r.cost]) AS `Station - Cost`;
//6: Show the length of all connected stations ordered shortest -> longest
MATCH (s1:Station)-[r:Type]->(s2:Station)
  WHERE (s1.StationName = 'S+U Berlin Hauptbahnhof')
RETURN s1.StationName AS `Station 1`, s2.StationName AS `Station 2`, sum(r.cost) AS `Total Cost`
  ORDER BY `Total Cost` ASC;