#!/usr/bin/env bash

"$(pwd)"/trec_eval.9.0/trec_eval -q -m all_trec "$(pwd)"/qrels.assignment2 "$(pwd)"/DataSet/queryResults > "$(pwd)"/output.txt
