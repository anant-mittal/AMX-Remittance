#!/bin/sh

source ./config/vars.sh

file="~/config/vars.sh"

if [ -f ~/config/vars.sh ]
then
        source ~/config/vars.sh
else
        echo "CONFIG FILE NOT FOUND"
fi
