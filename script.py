#!/ usr/bin/env python
""" script.py """

import sys
sys.stdout.write("create \"istepanian:trees\", {NAME => \"species\", VERSIONS => 2}, {NAME => \"information\", VERSIONS => 2}, {NAME => \"address\", VERSIONS => 2};\n")

count = 0
for line in sys.stdin:
    if count > 0:
        fields = line.split(";")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"species:GENRE\", \"" + fields[2] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"species:ESPECE\", \"" + fields[3] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"species:FAMILLE\", \"" + fields[4] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"species:NOM COMMUN\", \"" + fields[9] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"species:VARIETE\", \"" + fields[10] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"information:ANNEE PLANTATION\", \"" + fields[2] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"information:HAUTEUR\", \"" + fields[2] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"information:CIRCONFERENCE\", \"" + fields[2] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"address:GEOPOINT\", \"" + fields[2] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"address:ARRONDISSEMENT\", \"" + fields[2] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"address:ADRESSE\", \"" + fields[2] + "\";\n")
        sys.stdout.write("put \"istepanian:trees\", \"" + fields[11] + "\", \"address:NOM_EV\", \"" + fields[2] + "\";\n")

    count += 1