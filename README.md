# Microservices Playground Readme

**Disclaimer:** This is my playground for playing around with different things related to microservices. It is not
meant as a demo or example of best practices, nor should you assume that anything is finished or directly usable in
production. That being said, this repository may very well contain small bits and pieces that can be refined into 
something that is in fact usable in production.

## Trying out the applications

First of all you have to use Java 11 to run these applications.

Second, even though the order in which the applications start up should not matter in a microservice system, this playground
is not really there yet. Therefore, to try out the applications, you should start them in the following order:

1. *Service Directory Server*: `net.pkhapps.playground.microservices.directory.server.ServiceDirectoryServer`, will run on
   port 9999 and use an H2 database that lives in the current user's home directory.
2. *Frontend Portal Sample 1*: `net.pkhapps.playground.microservices.portal.sample.app1.SampleApp1`, will run on port 8200.
3. *Frontend Portal Sample 2*: `net.pkhapps.playground.microservices.portal.sample.app2.SampleApp2`, will run on port 8201.
4. *Frontend Portal Server*: `net.pkhapps.playground.microservices.portal.server.FrontendPortalServer`, will run on port 8888.

Once everything is up and running, you can access the UI by browsing to http://localhost:8888.

## A note about session cookies

Session cookies are isolated by hostname and context path, not by port number. This means that in order to avoid conflicts
between the session cookies of the portal server and the two frontend sample applications, they must all either have
different host names or different context paths. It does not matter that they are all running as separate applications
on separate ports. In this case, the portal server has the default `/` context path, whereas the example frontend 
applications have the context paths `/app1` and `/app2`.

## A note about security

I'm playing with using public-private key pairs when registering new services/frontends with the directory server. The
idea is that an administrator registers a resource and a public key up front, and then the individual resource instances
use their private keys to sign their registration requests.

However, there is currently no way for admins to register the public keys, nor is there any security check that prevents
unauthorized users from registering public keys. Therefore, the sample applications themselves will auto-generate a
key pair, then register a public key and finally register themselves. This is obviously something that you should never
do in a production environment.
