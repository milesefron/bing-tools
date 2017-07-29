# bing-tools

Tools for interacting with the Bing web search API

This repo contains client code for interacting with the Bing web search API (v5.0). In addition to running queries, paging through results, etc., the code supports manipulation of search result data in a few ways.

First, a client using this repo can undertake various forms of relevance feedback such as (a variant of) Lavrenko's relevance model (RM1), and simple Rocchio psuedo-feedback.

Additionally, the code supports pre-processing and data export for using third party software such as R on search results.
