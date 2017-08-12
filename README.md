# bing-tools

A simple set of tools for running searches using the Bing Web Search API and manipulating the resulting data. Part of the goal in designing these tools was minimizing the number of third-party libraries needed to make use of the Bing API.  To build and run, this library only relies on Google's `json-simple`, with `JUnit` required to build and run the tests.

This repo contains client code for interacting with the Bing web search API (v5.0). In addition to running queries, paging through results, etc., the code supports manipulation of search result data in a few ways.

First, a client using this repo can undertake various forms of relevance feedback such as (a variant of) Lavrenko's relevance model (RM1) (see the `ir.RelevanceModel` class).

Additionally, the code supports pre-processing and data export for using third party software such as R on search results (see the `data.CSV` class).

### Usage
For examples on using this library, see the classes in the `demo` package.

In terms of running queries and getting the results, most clients will do something like so:

```java
BingSearch = new BingSearch(<insert API key here>);
search.runQuery(<keyword query to run>);
String resultsAsJson = search.getResultsAsJson();
List<SearchHit> hits = JsonToSearchHits(json);
```
