#! /usr/bin/python

import hmac
import os.path, sys, time, mimetypes, xmlrpclib, pprint, base64
pp = pprint.PrettyPrinter()

config = {
#  'url': 'http://localhost.domd/~anj/drupals/drupal-7.0-beta3/services/xmlrpc',
  'url': 'http://localhost.domd/~anj/drupals/drupal-6/services/xmlrpc',
  'username': 'admin',
  'password': 'web-4anj',
#  'key': '05a665ffbee0436af1fb1918ee903f3c',
}

# Make initial connection to service, then login as developer
server = xmlrpclib.Server(config['url'], allow_none=True);
connection = server.system.connect();

# hash_hmac('sha256', $timestamp .';'.$domain .';'. $nonce .';'.'user.get', 'remote_api_key');
#h = hmac.new(key, data, digest_module)
#result = h.hexdigest()

#session = server.user.login( config['username'], config['password']);
#session = server.user.login(config['key'], 'localhost.domd', '', 'C7nW83nDw', connection['sessid'], config['username'], config['password']);
session = server.user.login(connection['sessid'],config['username'], config['password']);
sessid = session['sessid'];
user = session['user'];

timestamp = str(int(time.time()))

## Load a movie file
#filename = 'testfile.MOV'
#filesize = os.stat(filename).st_size
#filemime = mimetypes.guess_type(filename)
#fd = open(filename, 'rb')
#video_file = fd.read()
#fd.close()

# Create a file_obj dict with encoded file data
#file_obj = {
#    'file': base64.b64encode(video_file),
#    'filename': filename,
#    'filepath': 'sites/default/files/' + filename,
#    'filesize': filesize,
#    'timestamp': timestamp,
#    'uid': user['uid'],
#    'filemime': filemime,
#}

# Save the file to the server
#try:
#    f = server.file.save(sessid, file_obj)
#
#except xmlrpclib.Fault, err:
#    print "A fault occurred"
#    print "Fault code: %d" % err.faultCode
#    print "Fault string: %s" % err.faultString
#
#else:
#    pp.pprint(f) # DEBUG print new file id (fid)

'''
# Get the new file from the server (verify)  DEBUG - not needed - but shows how to retrieve a file based on fid
try:
    ff = server.file.get(sessid, f)

except xmlrpclib.Fault, err:
    print "A fault occurred"
    print "Fault code: %d" % err.faultCode
    print "Fault string: %s" % err.faultString

else:
    # pp.pprint(ff) # DEBUG - dump the file structure - including the file data
'''
#node = server.node.get(config['key'], 'localhost', '', 'C7nW8P3nDw', connection['sessid'],1)
node = server.node.get(sessid,1,{})
pp.pprint(node)

print "----"

# Create the node object and reference the new fid just created
node = {
  'type': 'format',
  'status': 1,
  'promote': 1,
#  'nid': 2,
  'title': 'Remote Test ' + timestamp,
  'body': 'This is a test created from a remote app. Easy.',
  'uid': user['uid'],
  'name': user['name'],
  'changed': timestamp,
  'field_shortname' : [
    {'value': 'shortname'},
  ],
  'field_version' : [
    {'value': 'Newest'},
  ],
  'field_puid' : [
    {'value': 'fmt/12'},
  ],
#  'files': { f: {
#    'new': 1, # Required to insert the referenced file->fid as new attachment!
#    'fid': f, # f is fid from uploaded video file
#    'list': 1, # Or 1, depending on whether you want the attachment listed on node view.
#    'description': 'Video File', # Any text description
#    'weight': 0,
#    }
#  },
}

pp.pprint(node) # DEBUG - dump the node - shows exact format to use in other languages

try:
    n = server.node.save(sessid, node)
    print n, node
    nn = server.node.get(sessid,n,{})  # DEBUG - get the final node - not needed now that we know it works

except xmlrpclib.Fault, err:
    print "A fault occurred"
    print "Fault code: %d" % err.faultCode
    print "Fault string: %s" % err.faultString

else:
    pp.pprint(n) # DEBUG
    pp.pprint(nn) # DEBUG - dump the final node - not needed now that we know it works