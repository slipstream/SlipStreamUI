#!/usr/bin/env python

import sys

if len(sys.argv) != 3:
    print '''
Usage: %s <base lang> <target lang>

Compare <base lang> and <target lang> to show which translations are missing in
<target lang> regarding to <base lang>.\n
''' % sys.argv[0]
    exit(1)


import re

class EnhancedList(list):
    def __init__(self, *args):
        super(EnhancedList, self).__init__(args)

    def __sub__(self, other):
        return self.__class__(*[item for item in self if item not in other])
        
    def __and__(self, other):
        return self.__class__(*[item for item in self if item in other])


base_lang = sys.argv[1]
target_lang = sys.argv[2]

translation_id_regex = re.compile(r'^[ \t]*(:[^ \t]+)[ \t]+[^ \t]+', re.MULTILINE)

with open('clj/resources/lang/%s.edn' % base_lang) as f:
    base_lang_file = f.read()

with open('clj/resources/lang/%s.edn' % target_lang) as f:
    target_lang_file = f.read()

base_translation_ids = EnhancedList(*translation_id_regex.findall(base_lang_file))
target_translation_ids = EnhancedList(*translation_id_regex.findall(target_lang_file))

missing_translations = base_translation_ids - target_translation_ids
common_translations = base_translation_ids & target_translation_ids
overly_translations = target_translation_ids - base_translation_ids

print '''
File:
    %s contains %s translations.
    %s contains %s translations.

There is: 
    %s translations in common.
    %s overly translations.
    %s missing translations.
''' % (base_lang, len(base_translation_ids), 
       target_lang, len(target_translation_ids), 
       len(common_translations), 
       len(overly_translations), 
       len(missing_translations))

print 'List of overly translations:'
for translation_id in overly_translations:
    print '    ' + translation_id

print ''

print 'List of missing translations:'
for translation_id in missing_translations:
    print '    ' + translation_id














