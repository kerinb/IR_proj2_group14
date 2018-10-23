#!/usr/bin/env bash

"$(pwd)"/trec_eval/trec_eval -m all_trec "$(pwd)"/cran/cranqrel "$(pwd)"/cran/cranQueryResults > "$(pwd)"/output.txt