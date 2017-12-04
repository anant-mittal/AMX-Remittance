#!/bin/sh

source ./config/vars.sh

file="~/config/vars.sh"

if [ -f "$file" ]
then
        source $file
fi
