#!/usr/bin/env bash

"$(pwd)"/trec_eval/trec_eval -m all_trec "$(pwd)"/qrels.assignment2A "$(pwd)"/DataSet/querryResults > "$(pwd)"/output.txt

