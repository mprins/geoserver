.. _geofence_configuration:

GeoFence Admin GUI
==================

The GeoFence Admin Page is a component of the GeoServer web interface. You can access it from the GeoServer web interface by clicking the :guilabel:`GeoFence` link, found on the left side of the screen after logging in.

.. figure:: images/configuration.png
   :align: center

General Settings
----------------
Configure the following settings here:

- GeoServer instance name: the name under which this GeoServer is known by the GeoFence server. This is useful for when you use an external GeoFence server with multiple GeoServer servers.

- GeoServer services URL: this is how GeoServer knows how to connect to the external GeoFence server. When using an internal GeoFence server, this is not configurable. For example "http://localhost:9191/geofence/remoting/RuleReader" for an external GeoFence server on localhost.


Options
-------

Configure the following settings here:

- Allow remote and inline layers in SLD

- Authenticated users can write

- Use GeoServer roles to get authorizations
  
   - Disabled: For each authorization request, GeoServer sends only the user info to GeoFence.
      GeoFence will retrieve all the roles associated with the user, and will merge 
      the permissions granted for each role.

   - Enabled: For each authorization request, GeoServer sends to GeoFence the user info AND the roles assigned in the current request session.
      GeoFence will retrieve all the roles associated to the user, and will only consider the requested roles 
      that are really associated to the user.    

- Comma delimited list of mutually exclusive roles for authorization

   - This field is mandatory when the previous option is enabled. 
      GeoServer will send to GeoFence the roles in the current request session which match the entries in this list.
      You can use the '*' symbol to match any session role.
      When using "*", you can use the format "`-ROLENAME`" to exclude one or more roles from the session roles list.
      
Cache
-----

Configure the following settings here:

- Size of the rule cache (number of entries)

- Cache refresh interval (ms)

- Cache expire interval (ms)

Collected data about the cache can be retrieved here. Per cache (rules, admin rules and users) we retrieve the cache size, hits, misses, load successes, load failures, load times and evictions. The cache can be manually invalidated (cleared).

Basic GeoServer configuration
-----------------------------

- Login with the default administrative credentials ``admin / geoserver`` (or whatever you have configured before). 

.. figure:: images/basic_001.png
   :align: center

- In the security panel you'll find the GeoFence link to the GeoFence security admin page

.. figure:: images/basic_002.png
   :align: center

- Open the GeoFence admin page; you'll get to this page:

   You can notice here the information that allow the GeoFence probe inside GeoServer to communicate with the GeoFence engine:

   - the URL that the probe shall use to communicate with GeoFence;
   - the name (default is `default-gs`) this instance will use to identify itself to GeoFence. This instance name should be equal to the one we set into GeoFence.

- Testing connection to GeoFence.

   We already performed a connection test from GeoFence to GeoServer. Using the button **Test connection** we can also test that GeoServer can communicate to GeoFence. If everything is ok, you'll get this message:
   
   .. figure:: images/basic_003.png
      :align: center

- Open the **Authentication** page under the **Security** settings:

.. figure:: images/basic_004.png
   :align: center

- Add the GeoFence authenticator and **put it as the first in the list** otherwise you will not be able to login as ``admin/admin``:

.. figure:: images/basic_005.png
   :align: center

- Now that we added GeoFence as authentication provider, we'll be able to log into GeoServer using the credentials we added in GeoFence (user ``admin`` and user `tiger`). Try and log in using user ``tiger``.

Testing authorization
---------------------

- Logging into GeoServer as admin you will be able to see all the defined layers:

.. figure:: images/basic_006.png
   :align: center

- Logging into GeoServer as a non-admin user, the defined rules will be examined; since we defined no rules yet, the default behaviour is to deny access to all resources: 

.. figure:: images/basic_007.png
   :align: center

- Get back to GeoFence, and add a rule which allows all layers in workspace tiger for user ``tiger``; create a rule defining:

    - user ``tiger``
    - instance ``default-gs``
    - workspace ``tiger`` (you will get a dropdown menu containing all the workspaces available in the selected instance)
    - grant type: ``allow`` You'll get a line like this one:

    .. figure:: images/basic_008.png
       :align: center

- Verify the new authorizations.

    Since the probe caches the GeoFence responses, you may need to login again as administrator (or you may keep an admin session open in another browser) and clear the probe cache. You can do it by pressing the "Invalidate" button in the bottom of the GeoFence admin page:
    
    .. figure:: images/basic_009.png
       :align: center

- Login again in GeoServer as user tiger and you will see in **layer preview** all the layers in the ``tiger`` workspace:

.. figure:: images/basic_010.png
   :align: center
