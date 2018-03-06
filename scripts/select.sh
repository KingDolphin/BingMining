#!/bin/bash
sqlite3 ../db ".mode column" ".headers on" "SELECT * FROM accounts;"

