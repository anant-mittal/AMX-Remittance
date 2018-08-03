#!/bin/bash

git co staging
git up

branches=()
eval "$(git for-each-ref --shell --format='branches+=(%(refname))' refs/heads/)"

for branch in "${branches[@]}"
do
   branchc=${branch:11}
   echo $branchc
   git co $branchc
   git up
   git merge staging
   git mergetool
   git pu
done

#git co staging
