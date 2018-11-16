#!/usr/bin/env bash

"$(pwd)"/trec_eval/trec_eval -m all_trec "$(pwd)"/DataSet/qrels "$(pwd)"/DataSet/queryResults > "$(pwd)"/output.txt