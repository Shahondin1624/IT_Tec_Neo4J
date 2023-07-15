#!/bin/sh
# Start the Neo4j server
bin/neo4j start
# Wait for 10 seconds to allow the server to start up
sleep 10
# Execute the Cypher script
cat /var/lib/neo4j/import/creation.cypher | bin/cypher-shell -u neo4j -p 12345678
# Wait indefinitely to keep the container running
tail -f /dev/null
