# Overview

Docker Compose for Elasticsearch Cluster and Kibana instance for development propouses.

### Starting the Stack
```bash
docker-compose up
```

if you see this message "`max virtual memory areas vm.max_map_count [65530] likely too low, increase to at least [262144]`" on elasticsearch startup, you can increase the max_map using the command listed below, because by default, the amount of Virtual Memory is not enough.

Linux:
```bash
sudo sysctl -w vm.max_map_count=262144
```

Windows or Mac
```bash
docker-machine ssh
sudo sysctl -w vm.max_map_count=262144
```

**NOTE**: The `vm.max_map_count` will be reseted to default value after you reboot your machine

### Playing

Create a new index if it doesn't exists and adds a new document with ID 1

```bash
curl -XPUT "http://elasticsearch:9200/user/_doc/1" -H 'Content-Type: application/json' -d'
{
  "firstName":"Rafael",
  "lastName":"Silvestri"
}'
```

Retrieve the document 1
```bash
curl -XGET "http://elasticsearch:9200/user/_doc/1"
```

## Indexing documents in bulk

If you have a lot of documents to index, you can submit them in batches with the bulk API.
```bash
curl -H "Content-Type: application/json" -XPOST "http://localhost:9200/bank/_bulk?pretty&refresh" --data-binary "@accounts.json"
```

To retrieve a list of contacts where `gender` is M:

```bash
curl -XGET "http://elasticsearch:9200/bank/_search" -H 'Content-Type: application/json' -d '{ "from": 0, "size": 100, "query": {    "match": {"gender": "M"} }}'
```

### Index info

To catch more information about your indices, type:

```bash
curl "http://localhost:9200/_cat/indices?v"
```

### References  

https://medium.com/@maxy_ermayank/hands-on-elasticsearch-8fa59d8aebfc 

https://www.elastic.co/guide/en/elasticsearch/reference/current/getting-started-index.html