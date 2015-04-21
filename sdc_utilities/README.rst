SDC Utils for updating image metadata
============

This functionality updates the image metadata for the products uploaded
in the SDC software catalogue, when there are several regions. It objectives
is to syncronize the image filter in the products for all regions. To do that, it considers the image metadata
in a concrete region (Spain or Spain2), it obtains the image names, and look for
the image ID in the rest of regions. Finally, it updates the image metadata in the
catalogue.

Installation
=============

It is just needed to export some environment variables for configuring
against the keystone and then run the script.

.. code::

    #environment variables
    $ export OS_KEYSTONE=XXX
    $ export OS_PASSWORD=XXX
    $ export OS_USERNAME=XXX
    $ export OS_TENANT_NAME=XXX000000000000000000000000000081

    # Execute script
    $ pip install -r requirements.txt
    $ python update_image_filter.py

