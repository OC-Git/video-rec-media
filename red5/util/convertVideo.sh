#!/bin/bash

ffmpeg -i $1 -r 20 -ar 44100 -ac 1 -b 300k $2 > $1.log 2>&1
