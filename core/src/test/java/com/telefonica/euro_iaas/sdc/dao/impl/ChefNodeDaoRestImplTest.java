/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.telefonica.euro_iaas.sdc.dao.impl;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

import com.telefonica.euro_iaas.sdc.dao.ChefClientConfig;
import com.telefonica.euro_iaas.sdc.exception.CanNotCallChefException;
import com.telefonica.euro_iaas.sdc.keystoneutils.OpenStackRegion;
import com.telefonica.euro_iaas.sdc.model.dto.ChefNode;
import com.telefonica.euro_iaas.sdc.util.MixlibAuthenticationDigester;
import com.telefonica.euro_iaas.sdc.util.SystemPropertiesProvider;
import com.telefonica.fiware.commons.dao.EntityNotFoundException;
import com.telefonica.fiware.commons.openstack.auth.exception.OpenStackException;

/**
 * Unitary tests for ChefNodeDaoRestImpl.
 * 
 * @author jesus.movilla
 */
public class ChefNodeDaoRestImplTest {

    /**
     * Testing loadNode method.
     * 
     * @throws CanNotCallChefException
     * @throws OpenStackException
     */
    @Test
    public void shouldLoadNode() throws CanNotCallChefException, OpenStackException {
        // given
        ChefNodeDaoRestImpl chefNodeDaoRestImpl = new ChefNodeDaoRestImpl();
        String chefNodeName = "hostname.domain";
        SystemPropertiesProvider propertiesProvider = mock(SystemPropertiesProvider.class);
        MixlibAuthenticationDigester mixlibAuthenticationDigester = mock(MixlibAuthenticationDigester.class);
        OpenStackRegion openStackRegion = mock(OpenStackRegion.class);
        Client client = mock(Client.class);
        ChefClientConfig clientConfig = mock(ChefClientConfig.class);

        chefNodeDaoRestImpl.setPropertiesProvider(propertiesProvider);
        chefNodeDaoRestImpl.setDigester(mixlibAuthenticationDigester);
        chefNodeDaoRestImpl.setClientConfig(clientConfig);
        chefNodeDaoRestImpl.setOpenStackRegion(openStackRegion);

        WebTarget webResource = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Response response = mock(Response.class);

        String responseString = "{\n" + "  \"json_class\": \"Chef::Node\",\n" + "  \"override\": {\n" + "    \n"
                + "  },\n" + "  \"chef_environment\": \"_default\",\n" + "  \"chef_type\": \"node\",\n"
                + "  \"normal\": {\n" + "    \"tags\": [\n" + "      \n" + "    ]\n" + "  },\n" + "  \"run_list\": [\n"
                + "    \n" + "  ],\n" + "  \"automatic\": {\n" + "    \"os\": \"linux\",\n" + "    \"cpu\": {\n"
                + "      \"real\": 0,\n" + "      \"0\": {\n" + "        \"model\": \"2\",\n"
                + "        \"flags\": [\n" + "          \"fpu\",\n" + "          \"de\",\n" + "          \"pse\",\n"
                + "          \"tsc\",\n" + "          \"msr\",\n" + "          \"pae\",\n" + "          \"mce\",\n"
                + "          \"cx8\",\n" + "          \"apic\",\n" + "          \"mtrr\",\n" + "          \"pge\",\n"
                + "          \"mca\",\n" + "          \"cmov\",\n" + "          \"pse36\",\n"
                + "          \"clflush\",\n" + "          \"mmx\",\n" + "          \"fxsr\",\n"
                + "          \"sse\",\n" + "          \"sse2\",\n" + "          \"syscall\",\n" + "          \"nx\",\n"
                + "          \"lm\",\n" + "          \"up\",\n" + "          \"rep_good\",\n"
                + "          \"unfair_spinlock\",\n" + "          \"pni\",\n" + "          \"vmx\",\n"
                + "          \"cx16\",\n" + "          \"popcnt\",\n" + "          \"hypervisor\",\n"
                + "          \"lahf_lm\"\n" + "        ],\n" + "        \"mhz\": \"2394.230\",\n"
                + "        \"model_name\": \"QEMU Virtual CPU version 1.0\",\n" + "        \"family\": \"6\",\n"
                + "        \"vendor_id\": \"GenuineIntel\",\n" + "        \"stepping\": \"3\",\n"
                + "        \"cache_size\": \"4096 KB\"\n" + "      },\n" + "      \"total\": 1\n" + "    },\n"
                + "    \"current_user\": \"root\",\n" + "    \"ip6address\": \"fe80::f816:3eff:fe5c:ced3\",\n"
                + "    \"virtualization\": {\n" + "      \"role\": \"guest\",\n" + "      \"system\": \"kvm\"\n"
                + "    },\n" + "    \"languages\": {\n" + "      \"ruby\": {\n"
                + "        \"bin_dir\": \"\\/usr\\/bin\",\n" + "        \"host_os\": \"linux-gnu\",\n"
                + "        \"target_os\": \"linux\",\n" + "        \"host\": \"x86_64-redhat-linux-gnu\",\n"
                + "        \"host_cpu\": \"x86_64\",\n" + "        \"version\": \"1.8.7\",\n"
                + "        \"host_vendor\": \"redhat\",\n" + "        \"target\": \"x86_64-redhat-linux-gnu\",\n"
                + "        \"gem_bin\": \"\\/usr\\/bin\\/gem\",\n" + "        \"platform\": \"x86_64-linux\",\n"
                + "        \"release_date\": \"2011-06-30\",\n" + "        \"ruby_bin\": \"\\/usr\\/bin\\/ruby\",\n"
                + "        \"target_cpu\": \"x86_64\",\n"
                + "        \"gems_dir\": \"\\/usr\\/lib64\\/ruby\\/gems\\/1.8\",\n"
                + "        \"target_vendor\": \"redhat\"\n" + "      },\n" + "      \"perl\": {\n"
                + "        \"version\": \"5.10.1\",\n" + "        \"archname\": \"x86_64-linux-thread-multi\"\n"
                + "      },\n" + "      \"python\": {\n" + "        \"builddate\": \"Dec 7 2011, 20:48:22\",\n"
                + "        \"version\": \"2.6.6\"\n" + "      },\n" + "      \"lua\": {\n"
                + "        \"version\": \"5.1.4\"\n" + "      },\n" + "      \"java\": {\n"
                + "        \"runtime\": {\n"
                + "          \"name\": \"OpenJDK Runtime Environment (IcedTea6 1.10.4)\",\n"
                + "          \"build\": \"rhel-1.41.1.10.4.el6-x86_64\"\n" + "        },\n"
                + "        \"version\": \"1.6.0_22\",\n" + "        \"hotspot\": {\n"
                + "          \"name\": \"OpenJDK 64-Bit Server VM\",\n"
                + "          \"build\": \"20.0-b11, mixed mode\"\n" + "        }\n" + "      }\n" + "    },\n"
                + "    \"lsb\": {\n" + "      \"release\": \"6.2\",\n" + "      \"id\": \"CentOS\",\n"
                + "      \"description\": \"CentOS release 6.2 (Final)\",\n" + "      \"codename\": \"Final\"\n"
                + "    },\n" + "    \"domain\": \"novalocal\",\n" + "    \"platform_version\": \"6.2\",\n"
                + "    \"platform_family\": \"rhel\",\n" + "    \"roles\": [\n" + "      \n" + "    ],\n"
                + "    \"memory\": {\n" + "      \"writeback\": \"0kB\",\n" + "      \"inactive\": \"103500kB\",\n"
                + "      \"vmalloc_chunk\": \"34359731080kB\",\n" + "      \"dirty\": \"116kB\",\n"
                + "      \"vmalloc_used\": \"3528kB\",\n" + "      \"page_tables\": \"3984kB\",\n"
                + "      \"committed_as\": \"193392kB\",\n" + "      \"free\": \"286480kB\",\n"
                + "      \"slab\": \"27276kB\",\n" + "      \"nfs_unstable\": \"0kB\",\n"
                + "      \"anon_pages\": \"112640kB\",\n" + "      \"active\": \"70288kB\",\n" + "      \"swap\": {\n"
                + "        \"free\": \"0kB\",\n" + "        \"cached\": \"0kB\",\n" + "        \"total\": \"0kB\"\n"
                + "      },\n" + "      \"buffers\": \"11468kB\",\n" + "      \"bounce\": \"0kB\",\n"
                + "      \"commit_limit\": \"251360kB\",\n" + "      \"vmalloc_total\": \"34359738367kB\",\n"
                + "      \"mapped\": \"12436kB\",\n" + "      \"slab_reclaimable\": \"7728kB\",\n"
                + "      \"cached\": \"49652kB\",\n" + "      \"total\": \"502720kB\",\n"
                + "      \"slab_unreclaim\": \"19548kB\"\n" + "    },\n"
                + "    \"idletime\": \"10 minutes 11 seconds\",\n" + "    \"command\": {\n"
                + "      \"ps\": \"ps -ef\"\n" + "    },\n" + "    \"idletime_seconds\": 611,\n"
                + "    \"ohai_time\": 2381841405.7853,\n" + "    \"uptime\": \"10 minutes 55 seconds\",\n"
                + "    \"dmi\": {\n" + "      \"chassis\": {\n" + "        \"lock\": \"Not Present\",\n"
                + "        \"type\": \"Other\",\n" + "        \"asset_tag\": \"Not Specified\",\n"
                + "        \"version\": \"Not Specified\",\n" + "        \"power_supply_state\": \"Safe\",\n"
                + "        \"security_status\": \"Unknown\",\n" + "        \"boot_up_state\": \"Safe\",\n"
                + "        \"height\": \"Unspecified\",\n" + "        \"all_records\": [\n" + "          {\n"
                + "            \"Lock\": \"Not Present\",\n" + "            \"size\": \"3\",\n"
                + "            \"Serial Number\": \"Not Specified\",\n"
                + "            \"Version\": \"Not Specified\",\n" + "            \"Boot-up State\": \"Safe\",\n"
                + "            \"Asset Tag\": \"Not Specified\",\n" + "            \"Type\": \"Other\",\n"
                + "            \"Security Status\": \"Unknown\",\n"
                + "            \"Number Of Power Cords\": \"Unspecified\",\n"
                + "            \"application_identifier\": \"Chassis Information\",\n"
                + "            \"Power Supply State\": \"Safe\",\n" + "            \"record_id\": \"0x0300\",\n"
                + "            \"OEM Information\": \"0x00000000\",\n" + "            \"Height\": \"Unspecified\",\n"
                + "            \"Thermal State\": \"Safe\",\n" + "            \"Manufacturer\": \"Bochs\"\n"
                + "          }\n" + "        ],\n" + "        \"serial_number\": \"Not Specified\",\n"
                + "        \"manufacturer\": \"Bochs\",\n" + "        \"number_of_power_cords\": \"Unspecified\",\n"
                + "        \"oem_information\": \"0x00000000\",\n" + "        \"thermal_state\": \"Safe\"\n"
                + "      },\n" + "      \"structures\": {\n" + "        \"size\": \"263\",\n"
                + "        \"count\": \"10\"\n" + "      },\n" + "      \"dmidecode_version\": \"2.11\",\n"
                + "      \"processor\": {\n" + "        \"locator\": \"DIMM 0\",\n"
                + "        \"status\": \"No errors detected\",\n" + "        \"bank_locator\": \"Not Specified\",\n"
                + "        \"voltage\": \"Unknown\",\n" + "        \"max_speed\": \"2000 MHz\",\n"
                + "        \"size\": \"512 MB\",\n" + "        \"l2_cache_handle\": \"Not Provided\",\n"
                + "        \"form_factor\": \"DIMM\",\n" + "        \"data_width\": \"64 bits\",\n"
                + "        \"type\": \"RAM\",\n" + "        \"range_size\": \"512 MB\",\n"
                + "        \"total_width\": \"64 bits\",\n" + "        \"physical_device_handle\": \"0x1100\",\n"
                + "        \"number_of_devices\": \"1\",\n" + "        \"id\": \"23 06 00 00 FD FB 8B 07\",\n"
                + "        \"maximum_capacity\": \"512 MB\",\n" + "        \"partition_width\": \"1\",\n"
                + "        \"type_detail\": \"None\",\n" + "        \"l1_cache_handle\": \"Not Provided\",\n"
                + "        \"partition_row_position\": \"1\",\n" + "        \"physical_array_handle\": \"0x1000\",\n"
                + "        \"version\": \"Not Specified\",\n" + "        \"use\": \"System Memory\",\n"
                + "        \"error_correction_type\": \"Multi-bit ECC\",\n"
                + "        \"starting_address\": \"0x00000000000\",\n" + "        \"all_records\": [\n"
                + "          {\n" + "            \"Array Handle\": \"0x1000\",\n"
                + "            \"L1 Cache Handle\": \"Not Provided\",\n" + "            \"Voltage\": \"Unknown\",\n"
                + "            \"Locator\": \"DIMM 0\",\n" + "            \"ID\": \"23 06 00 00 FD FB 8B 07\",\n"
                + "            \"size\": \"4\",\n" + "            \"Set\": \"None\",\n"
                + "            \"Size\": \"512 MB\",\n" + "            \"Number Of Devices\": \"1\",\n"
                + "            \"Version\": \"Not Specified\",\n" + "            \"Status\": \"No errors detected\",\n"
                + "            \"Maximum Capacity\": \"512 MB\",\n" + "            \"Partition Width\": \"1\",\n"
                + "            \"Partition Row Position\": \"1\",\n" + "            \"Type Detail\": \"None\",\n"
                + "            \"External Clock\": \"Unknown\",\n"
                + "            \"Memory Array Mapped Address Handle\": \"0x1300\",\n"
                + "            \"Bank Locator\": \"Not Specified\",\n" + "            \"Total Width\": \"64 bits\",\n"
                + "            \"Current Speed\": \"2000 MHz\",\n" + "            \"Type\": \"RAM\",\n"
                + "            \"Data Width\": \"64 bits\",\n" + "            \"L3 Cache Handle\": \"Not Provided\",\n"
                + "            \"Socket Designation\": \"CPU 1\",\n" + "            \"Max Speed\": \"2000 MHz\",\n"
                + "            \"Range Size\": \"512 MB\",\n" + "            \"Form Factor\": \"DIMM\",\n"
                + "            \"Physical Array Handle\": \"0x1000\",\n" + "            \"Use\": \"System Memory\",\n"
                + "            \"application_identifier\": \"End Of Table\",\n"
                + "            \"Starting Address\": \"0x00000000000\",\n"
                + "            \"Ending Address\": \"0x0001FFFFFFF\",\n"
                + "            \"Physical Device Handle\": \"0x1100\",\n" + "            \"Upgrade\": \"Other\",\n"
                + "            \"record_id\": \"0x0401\",\n"
                + "            \"Error Information Handle\": \"0x0307\",\n"
                + "            \"Manufacturer\": \"Bochs\",\n" + "            \"L2 Cache Handle\": \"Not Provided\",\n"
                + "            \"Error Correction Type\": \"Multi-bit ECC\",\n"
                + "            \"Family\": \"Other\",\n" + "            \"Location\": \"Other\"\n" + "          }\n"
                + "        ],\n" + "        \"current_speed\": \"2000 MHz\",\n" + "        \"location\": \"Other\",\n"
                + "        \"socket_designation\": \"CPU 1\",\n" + "        \"manufacturer\": \"Bochs\",\n"
                + "        \"family\": \"Other\",\n" + "        \"set\": \"None\",\n"
                + "        \"memory_array_mapped_address_handle\": \"0x1300\",\n"
                + "        \"external_clock\": \"Unknown\",\n" + "        \"array_handle\": \"0x1000\",\n"
                + "        \"upgrade\": \"Other\",\n" + "        \"l3_cache_handle\": \"Not Provided\",\n"
                + "        \"ending_address\": \"0x0001FFFFFFF\",\n"
                + "        \"error_information_handle\": \"0x0307\"\n" + "      },\n"
                + "      \"smbios_version\": \"2.4\",\n" + "      \"bios\": {\n" + "        \"version\": \"Bochs\",\n"
                + "        \"bios_revision\": \"1.0\",\n" + "        \"all_records\": [\n" + "          {\n"
                + "            \"size\": \"0\",\n" + "            \"Characteristics\": {\n"
                + "              \"BIOS characteristics not supported\": null,\n"
                + "              \"Targeted content distribution is supported\": null\n" + "            },\n"
                + "            \"Release Date\": \"01\\/01\\/2007\",\n" + "            \"Runtime Size\": \"96 kB\",\n"
                + "            \"Version\": \"Bochs\",\n" + "            \"BIOS Revision\": \"1.0\",\n"
                + "            \"application_identifier\": \"BIOS Information\",\n"
                + "            \"record_id\": \"0x0000\",\n" + "            \"ROM Size\": \"64 kB\",\n"
                + "            \"Vendor\": \"Bochs\",\n" + "            \"Address\": \"0xE8000\"\n" + "          }\n"
                + "        ],\n" + "        \"runtime_size\": \"96 kB\",\n"
                + "        \"release_date\": \"01\\/01\\/2007\",\n" + "        \"address\": \"0xE8000\",\n"
                + "        \"rom_size\": \"64 kB\",\n" + "        \"vendor\": \"Bochs\"\n" + "      },\n"
                + "      \"system\": {\n" + "        \"wake_up_type\": \"Power Switch\",\n"
                + "        \"version\": \"Not Specified\",\n" + "        \"product_name\": \"Bochs\",\n"
                + "        \"all_records\": [\n" + "          {\n"
                + "            \"Wake-up Type\": \"Power Switch\",\n" + "            \"Product Name\": \"Bochs\",\n"
                + "            \"size\": \"1\",\n" + "            \"Serial Number\": \"Not Specified\",\n"
                + "            \"Version\": \"Not Specified\",\n"
                + "            \"UUID\": \"A378FD62-BBC8-4BA4-8C8D-8D196789708C\",\n"
                + "            \"SKU Number\": \"Not Specified\",\n"
                + "            \"application_identifier\": \"System Information\",\n"
                + "            \"record_id\": \"0x0100\",\n" + "            \"Manufacturer\": \"Bochs\",\n"
                + "            \"Family\": \"Not Specified\"\n" + "          }\n" + "        ],\n"
                + "        \"serial_number\": \"Not Specified\",\n" + "        \"manufacturer\": \"Bochs\",\n"
                + "        \"family\": \"Not Specified\",\n" + "        \"sku_number\": \"Not Specified\",\n"
                + "        \"uuid\": \"A378FD62-BBC8-4BA4-8C8D-8D196789708C\"\n" + "      }\n" + "    },\n"
                + "    \"block_device\": {\n" + "      \"ram0\": {\n" + "        \"size\": \"32768\",\n"
                + "        \"removable\": \"0\"\n" + "      },\n" + "      \"ram1\": {\n"
                + "        \"size\": \"32768\",\n" + "        \"removable\": \"0\"\n" + "      },\n"
                + "      \"ram2\": {\n" + "        \"size\": \"32768\",\n" + "        \"removable\": \"0\"\n"
                + "      },\n" + "      \"ram3\": {\n" + "        \"size\": \"32768\",\n"
                + "        \"removable\": \"0\"\n" + "      },\n" + "      \"ram4\": {\n"
                + "        \"size\": \"32768\",\n" + "        \"removable\": \"0\"\n" + "      },\n"
                + "      \"ram5\": {\n" + "        \"size\": \"32768\",\n" + "        \"removable\": \"0\"\n"
                + "      },\n" + "      \"ram6\": {\n" + "        \"size\": \"32768\",\n"
                + "        \"removable\": \"0\"\n" + "      },\n" + "      \"ram7\": {\n"
                + "        \"size\": \"32768\",\n" + "        \"removable\": \"0\"\n" + "      },\n"
                + "      \"ram8\": {\n" + "        \"size\": \"32768\",\n" + "        \"removable\": \"0\"\n"
                + "      },\n" + "      \"ram9\": {\n" + "        \"size\": \"32768\",\n"
                + "        \"removable\": \"0\"\n" + "      },\n" + "      \"vda\": {\n"
                + "        \"size\": \"10485760\",\n" + "        \"vendor\": \"6900\",\n"
                + "        \"removable\": \"0\"\n" + "      },\n" + "      \"loop0\": {\n"
                + "        \"size\": \"0\",\n" + "        \"removable\": \"0\"\n" + "      },\n"
                + "      \"loop1\": {\n" + "        \"size\": \"0\",\n" + "        \"removable\": \"0\"\n"
                + "      },\n" + "      \"loop2\": {\n" + "        \"size\": \"0\",\n"
                + "        \"removable\": \"0\"\n" + "      },\n" + "      \"loop3\": {\n"
                + "        \"size\": \"0\",\n" + "        \"removable\": \"0\"\n" + "      },\n"
                + "      \"loop4\": {\n" + "        \"size\": \"0\",\n" + "        \"removable\": \"0\"\n"
                + "      },\n" + "      \"loop5\": {\n" + "        \"size\": \"0\",\n"
                + "        \"removable\": \"0\"\n" + "      },\n" + "      \"loop6\": {\n"
                + "        \"size\": \"0\",\n" + "        \"removable\": \"0\"\n" + "      },\n"
                + "      \"loop7\": {\n" + "        \"size\": \"0\",\n" + "        \"removable\": \"0\"\n"
                + "      },\n" + "      \"ram10\": {\n" + "        \"size\": \"32768\",\n"
                + "        \"removable\": \"0\"\n" + "      },\n" + "      \"ram11\": {\n"
                + "        \"size\": \"32768\",\n" + "        \"removable\": \"0\"\n" + "      },\n"
                + "      \"ram12\": {\n" + "        \"size\": \"32768\",\n" + "        \"removable\": \"0\"\n"
                + "      },\n" + "      \"ram13\": {\n" + "        \"size\": \"32768\",\n"
                + "        \"removable\": \"0\"\n" + "      },\n" + "      \"ram14\": {\n"
                + "        \"size\": \"32768\",\n" + "        \"removable\": \"0\"\n" + "      },\n"
                + "      \"ram15\": {\n" + "        \"size\": \"32768\",\n" + "        \"removable\": \"0\"\n"
                + "      }\n" + "    },\n" + "    \"platform\": \"centos\",\n"
                + "    \"fqdn\": \"sdc15102013d.novalocal\",\n" + "    \"filesystem\": {\n" + "      \"\\/sys\": {\n"
                + "        \"mount\": \"\\/sys\",\n" + "        \"fs_type\": \"sysfs\",\n"
                + "        \"mount_options\": [\n" + "          \"rw\",\n" + "          \"relatime\"\n" + "        ]\n"
                + "      },\n" + "      \"\\/dev\\/vda1\": {\n" + "        \"mount\": \"\\/\",\n"
                + "        \"fs_type\": \"ext4\",\n" + "        \"percent_used\": \"44%\",\n"
                + "        \"kb_size\": \"5159552\",\n" + "        \"mount_options\": [\n" + "          \"rw\"\n"
                + "        ],\n" + "        \"uuid\": \"93ab5295-5527-4919-8098-ce9b49bc7a65\",\n"
                + "        \"kb_used\": \"2142816\",\n" + "        \"kb_available\": \"2754648\"\n" + "      },\n"
                + "      \"\\/proc\\/bus\\/usb\": {\n" + "        \"mount\": \"\\/proc\\/bus\\/usb\",\n"
                + "        \"fs_type\": \"usbfs\",\n" + "        \"mount_options\": [\n" + "          \"rw\",\n"
                + "          \"relatime\"\n" + "        ]\n" + "      },\n" + "      \"tmpfs\": {\n"
                + "        \"mount\": \"\\/dev\\/shm\",\n" + "        \"fs_type\": \"tmpfs\",\n"
                + "        \"percent_used\": \"0%\",\n" + "        \"kb_size\": \"251360\",\n"
                + "        \"mount_options\": [\n" + "          \"rw\"\n" + "        ],\n"
                + "        \"kb_used\": \"0\",\n" + "        \"kb_available\": \"251360\"\n" + "      },\n"
                + "      \"rootfs\": {\n" + "        \"mount\": \"\\/\",\n" + "        \"fs_type\": \"rootfs\",\n"
                + "        \"mount_options\": [\n" + "          \"rw\"\n" + "        ]\n" + "      },\n"
                + "      \"sysfs\": {\n" + "        \"mount\": \"\\/sys\",\n" + "        \"fs_type\": \"sysfs\",\n"
                + "        \"mount_options\": [\n" + "          \"rw\"\n" + "        ]\n" + "      },\n"
                + "      \"\\/proc\": {\n" + "        \"mount\": \"\\/proc\",\n" + "        \"fs_type\": \"proc\",\n"
                + "        \"mount_options\": [\n" + "          \"rw\",\n" + "          \"relatime\"\n" + "        ]\n"
                + "      },\n" + "      \"devpts\": {\n" + "        \"mount\": \"\\/dev\\/pts\",\n"
                + "        \"fs_type\": \"devpts\",\n" + "        \"mount_options\": [\n" + "          \"rw\",\n"
                + "          \"gid=5\",\n" + "          \"mode=620\"\n" + "        ]\n" + "      },\n"
                + "      \"\\/etc\\/auto.misc\": {\n" + "        \"mount\": \"\\/misc\",\n"
                + "        \"fs_type\": \"autofs\",\n" + "        \"mount_options\": [\n" + "          \"rw\",\n"
                + "          \"relatime\",\n" + "          \"fd=7\",\n" + "          \"pgrp=1402\",\n"
                + "          \"timeout=300\",\n" + "          \"minproto=5\",\n" + "          \"maxproto=5\",\n"
                + "          \"indirect\"\n" + "        ]\n" + "      },\n" + "      \"none\": {\n"
                + "        \"mount\": \"\\/proc\\/sys\\/fs\\/binfmt_misc\",\n"
                + "        \"fs_type\": \"binfmt_misc\",\n" + "        \"mount_options\": [\n" + "          \"rw\"\n"
                + "        ]\n" + "      },\n" + "      \"udev\": {\n" + "        \"mount\": \"\\/dev\",\n"
                + "        \"fs_type\": \"devtmpfs\",\n" + "        \"mount_options\": [\n" + "          \"rw\",\n"
                + "          \"relatime\",\n" + "          \"size=241740k\",\n" + "          \"nr_inodes=60435\",\n"
                + "          \"mode=755\"\n" + "        ]\n" + "      },\n" + "      \"proc\": {\n"
                + "        \"mount\": \"\\/proc\",\n" + "        \"fs_type\": \"proc\",\n"
                + "        \"mount_options\": [\n" + "          \"rw\"\n" + "        ]\n" + "      },\n"
                + "      \"sunrpc\": {\n" + "        \"mount\": \"\\/var\\/lib\\/nfs\\/rpc_pipefs\",\n"
                + "        \"fs_type\": \"rpc_pipefs\",\n" + "        \"mount_options\": [\n" + "          \"rw\"\n"
                + "        ]\n" + "      },\n" + "      \"-hosts\": {\n" + "        \"mount\": \"\\/net\",\n"
                + "        \"fs_type\": \"autofs\",\n" + "        \"mount_options\": [\n" + "          \"rw\",\n"
                + "          \"relatime\",\n" + "          \"fd=13\",\n" + "          \"pgrp=1402\",\n"
                + "          \"timeout=300\",\n" + "          \"minproto=5\",\n" + "          \"maxproto=5\",\n"
                + "          \"indirect\"\n" + "        ]\n" + "      }\n" + "    },\n" + "    \"kernel\": {\n"
                + "      \"release\": \"2.6.32-220.el6.x86_64\",\n" + "      \"os\": \"GNU\\/Linux\",\n"
                + "      \"machine\": \"x86_64\",\n" + "      \"version\": \"#1 SMP Tue Dec 6 19:48:22 GMT 2011\",\n"
                + "      \"modules\": {\n" + "        \"virtio_balloon\": {\n" + "          \"size\": \"4347\",\n"
                + "          \"refcount\": \"0\"\n" + "        },\n" + "        \"microcode\": {\n"
                + "          \"size\": \"112594\",\n" + "          \"refcount\": \"0\"\n" + "        },\n"
                + "        \"i2c_core\": {\n" + "          \"size\": \"31276\",\n" + "          \"refcount\": \"1\"\n"
                + "        },\n" + "        \"mbcache\": {\n" + "          \"size\": \"8144\",\n"
                + "          \"refcount\": \"1\"\n" + "        },\n" + "        \"ip6table_filter\": {\n"
                + "          \"size\": \"2889\",\n" + "          \"refcount\": \"1\"\n" + "        },\n"
                + "        \"i2c_piix4\": {\n" + "          \"size\": \"12608\",\n" + "          \"refcount\": \"0\"\n"
                + "        },\n" + "        \"ip6t_REJECT\": {\n" + "          \"size\": \"4628\",\n"
                + "          \"refcount\": \"2\"\n" + "        },\n" + "        \"ata_generic\": {\n"
                + "          \"size\": \"3837\",\n" + "          \"refcount\": \"0\"\n" + "        },\n"
                + "        \"virtio_pci\": {\n" + "          \"size\": \"6687\",\n" + "          \"refcount\": \"0\"\n"
                + "        },\n" + "        \"dm_log\": {\n" + "          \"size\": \"10122\",\n"
                + "          \"refcount\": \"2\"\n" + "        },\n" + "        \"dm_mod\": {\n"
                + "          \"size\": \"81500\",\n" + "          \"refcount\": \"2\"\n" + "        },\n"
                + "        \"nf_conntrack\": {\n" + "          \"size\": \"79453\",\n"
                + "          \"refcount\": \"2\"\n" + "        },\n" + "        \"ipv6\": {\n"
                + "          \"size\": \"322029\",\n" + "          \"refcount\": \"29\"\n" + "        },\n"
                + "        \"virtio_net\": {\n" + "          \"size\": \"15839\",\n"
                + "          \"refcount\": \"0\"\n" + "        },\n" + "        \"ata_piix\": {\n"
                + "          \"size\": \"22846\",\n" + "          \"refcount\": \"0\"\n" + "        },\n"
                + "        \"ip6_tables\": {\n" + "          \"size\": \"19458\",\n"
                + "          \"refcount\": \"1\"\n" + "        },\n" + "        \"xt_state\": {\n"
                + "          \"size\": \"1492\",\n" + "          \"refcount\": \"2\"\n" + "        },\n"
                + "        \"virtio_blk\": {\n" + "          \"size\": \"6671\",\n" + "          \"refcount\": \"2\"\n"
                + "        },\n" + "        \"nf_conntrack_ipv6\": {\n" + "          \"size\": \"8748\",\n"
                + "          \"refcount\": \"2\"\n" + "        },\n" + "        \"dm_mirror\": {\n"
                + "          \"size\": \"14101\",\n" + "          \"refcount\": \"0\"\n" + "        },\n"
                + "        \"autofs4\": {\n" + "          \"size\": \"26888\",\n" + "          \"refcount\": \"3\"\n"
                + "        },\n" + "        \"pata_acpi\": {\n" + "          \"size\": \"3701\",\n"
                + "          \"refcount\": \"0\"\n" + "        },\n" + "        \"dm_region_hash\": {\n"
                + "          \"size\": \"12170\",\n" + "          \"refcount\": \"1\"\n" + "        },\n"
                + "        \"nf_defrag_ipv6\": {\n" + "          \"size\": \"12182\",\n"
                + "          \"refcount\": \"1\"\n" + "        },\n" + "        \"ext4\": {\n"
                + "          \"size\": \"364410\",\n" + "          \"refcount\": \"1\"\n" + "        },\n"
                + "        \"virtio_ring\": {\n" + "          \"size\": \"7729\",\n"
                + "          \"refcount\": \"4\"\n" + "        },\n" + "        \"jbd2\": {\n"
                + "          \"size\": \"88738\",\n" + "          \"refcount\": \"1\"\n" + "        },\n"
                + "        \"sunrpc\": {\n" + "          \"size\": \"243758\",\n" + "          \"refcount\": \"1\"\n"
                + "        },\n" + "        \"virtio\": {\n" + "          \"size\": \"4890\",\n"
                + "          \"refcount\": \"4\"\n" + "        }\n" + "      },\n" + "      \"name\": \"Linux\"\n"
                + "    },\n" + "    \"hostname\": \"sdc15102013d\",\n" + "    \"network\": {\n"
                + "      \"default_interface\": \"eth0\",\n" + "      \"interfaces\": {\n" + "        \"eth0\": {\n"
                + "          \"type\": \"eth\",\n" + "          \"addresses\": {\n"
                + "            \"172.30.5.13\": {\n" + "              \"scope\": \"Global\",\n"
                + "              \"netmask\": \"255.255.255.0\",\n" + "              \"family\": \"inet\",\n"
                + "              \"broadcast\": \"172.30.5.255\",\n" + "              \"prefixlen\": \"24\"\n"
                + "            },\n" + "            \"fe80::f816:3eff:fe5c:ced3\": {\n"
                + "              \"scope\": \"Link\",\n" + "              \"family\": \"inet6\",\n"
                + "              \"prefixlen\": \"64\"\n" + "            },\n"
                + "            \"FA:16:3E:5C:CE:D3\": {\n" + "              \"family\": \"lladdr\"\n"
                + "            }\n" + "          },\n" + "          \"arp\": {\n"
                + "            \"172.30.5.1\": \"54:52:00:37:68:64\"\n" + "          },\n" + "          \"flags\": [\n"
                + "            \"BROADCAST\",\n" + "            \"MULTICAST\",\n" + "            \"UP\",\n"
                + "            \"LOWER_UP\"\n" + "          ],\n" + "          \"number\": \"0\",\n"
                + "          \"routes\": [\n" + "            {\n" + "              \"scope\": \"link\",\n"
                + "              \"destination\": \"172.30.5.0\\/24\",\n" + "              \"proto\": \"kernel\",\n"
                + "              \"src\": \"172.30.5.13\",\n" + "              \"family\": \"inet\"\n"
                + "            },\n" + "            {\n" + "              \"scope\": \"link\",\n"
                + "              \"destination\": \"169.254.0.0\\/16\",\n" + "              \"family\": \"inet\",\n"
                + "              \"metric\": \"1002\"\n" + "            },\n" + "            {\n"
                + "              \"via\": \"172.30.5.1\",\n" + "              \"destination\": \"default\",\n"
                + "              \"family\": \"inet\"\n" + "            },\n" + "            {\n"
                + "              \"destination\": \"fe80::\\/64\",\n" + "              \"proto\": \"kernel\",\n"
                + "              \"family\": \"inet6\",\n" + "              \"metric\": \"256\"\n" + "            }\n"
                + "          ],\n" + "          \"encapsulation\": \"Ethernet\",\n" + "          \"mtu\": \"1500\",\n"
                + "          \"state\": \"up\"\n" + "        },\n" + "        \"lo\": {\n"
                + "          \"addresses\": {\n" + "            \"::1\": {\n" + "              \"scope\": \"Node\",\n"
                + "              \"family\": \"inet6\",\n" + "              \"prefixlen\": \"128\"\n"
                + "            },\n" + "            \"127.0.0.1\": {\n" + "              \"scope\": \"Node\",\n"
                + "              \"netmask\": \"255.0.0.0\",\n" + "              \"family\": \"inet\",\n"
                + "              \"prefixlen\": \"8\"\n" + "            }\n" + "          },\n"
                + "          \"flags\": [\n" + "            \"LOOPBACK\",\n" + "            \"UP\",\n"
                + "            \"LOWER_UP\"\n" + "          ],\n" + "          \"routes\": [\n" + "            {\n"
                + "              \"destination\": \"unreachable\",\n" + "              \"family\": \"inet6\",\n"
                + "              \"metric\": \"1024\"\n" + "            }\n" + "          ],\n"
                + "          \"encapsulation\": \"Loopback\",\n" + "          \"mtu\": \"16436\",\n"
                + "          \"state\": \"unknown\"\n" + "        }\n" + "      },\n"
                + "      \"default_gateway\": \"172.30.5.1\"\n" + "    },\n" + "    \"keys\": {\n"
                + "      \"ssh\": {\n" + "        \n" + "      }\n" + "    },\n" + "    \"etc\": {\n"
                + "      \"passwd\": {\n" + "        \"bin\": {\n" + "          \"dir\": \"\\/bin\",\n"
                + "          \"gid\": 1,\n" + "          \"uid\": 1,\n" + "          \"gecos\": \"bin\",\n"
                + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n" + "        \"uucp\": {\n"
                + "          \"dir\": \"\\/var\\/spool\\/uucp\",\n" + "          \"gid\": 14,\n"
                + "          \"uid\": 10,\n" + "          \"gecos\": \"uucp\",\n"
                + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n" + "        \"haldaemon\": {\n"
                + "          \"dir\": \"\\/\",\n" + "          \"gid\": 68,\n" + "          \"uid\": 68,\n"
                + "          \"gecos\": \"HAL daemon\",\n" + "          \"shell\": \"\\/sbin\\/nologin\"\n"
                + "        },\n" + "        \"localadmin\": {\n" + "          \"dir\": \"\\/home\\/localadmin\",\n"
                + "          \"gid\": 500,\n" + "          \"uid\": 500,\n" + "          \"gecos\": \"\",\n"
                + "          \"shell\": \"\\/bin\\/bash\"\n" + "        },\n" + "        \"gopher\": {\n"
                + "          \"dir\": \"\\/var\\/gopher\",\n" + "          \"gid\": 30,\n" + "          \"uid\": 13,\n"
                + "          \"gecos\": \"gopher\",\n" + "          \"shell\": \"\\/sbin\\/nologin\"\n"
                + "        },\n" + "        \"vcsa\": {\n" + "          \"dir\": \"\\/dev\",\n"
                + "          \"gid\": 69,\n" + "          \"uid\": 69,\n"
                + "          \"gecos\": \"virtual console memory owner\",\n"
                + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n" + "        \"abrt\": {\n"
                + "          \"dir\": \"\\/etc\\/abrt\",\n" + "          \"gid\": 173,\n" + "          \"uid\": 173,\n"
                + "          \"gecos\": \"\",\n" + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n"
                + "        \"nfsnobody\": {\n" + "          \"dir\": \"\\/var\\/lib\\/nfs\",\n"
                + "          \"gid\": 65534,\n" + "          \"uid\": 65534,\n"
                + "          \"gecos\": \"Anonymous NFS User\",\n" + "          \"shell\": \"\\/sbin\\/nologin\"\n"
                + "        },\n" + "        \"daemon\": {\n" + "          \"dir\": \"\\/sbin\",\n"
                + "          \"gid\": 2,\n" + "          \"uid\": 2,\n" + "          \"gecos\": \"daemon\",\n"
                + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n" + "        \"saslauth\": {\n"
                + "          \"dir\": \"\\/var\\/empty\\/saslauth\",\n" + "          \"gid\": 76,\n"
                + "          \"uid\": 499,\n" + "          \"gecos\": \"\\\"Saslauthd user\\\"\",\n"
                + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n" + "        \"lp\": {\n"
                + "          \"dir\": \"\\/var\\/spool\\/lpd\",\n" + "          \"gid\": 7,\n"
                + "          \"uid\": 4,\n" + "          \"gecos\": \"lp\",\n"
                + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n" + "        \"tcpdump\": {\n"
                + "          \"dir\": \"\\/\",\n" + "          \"gid\": 72,\n" + "          \"uid\": 72,\n"
                + "          \"gecos\": \"\",\n" + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n"
                + "        \"games\": {\n" + "          \"dir\": \"\\/usr\\/games\",\n" + "          \"gid\": 100,\n"
                + "          \"uid\": 12,\n" + "          \"gecos\": \"games\",\n"
                + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n" + "        \"halt\": {\n"
                + "          \"dir\": \"\\/sbin\",\n" + "          \"gid\": 0,\n" + "          \"uid\": 7,\n"
                + "          \"gecos\": \"halt\",\n" + "          \"shell\": \"\\/sbin\\/halt\"\n" + "        },\n"
                + "        \"shutdown\": {\n" + "          \"dir\": \"\\/sbin\",\n" + "          \"gid\": 0,\n"
                + "          \"uid\": 6,\n" + "          \"gecos\": \"shutdown\",\n"
                + "          \"shell\": \"\\/sbin\\/shutdown\"\n" + "        },\n" + "        \"oprofile\": {\n"
                + "          \"dir\": \"\\/home\\/oprofile\",\n" + "          \"gid\": 16,\n"
                + "          \"uid\": 16,\n"
                + "          \"gecos\": \"Special user account to be used by OProfile\",\n"
                + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n" + "        \"rpc\": {\n"
                + "          \"dir\": \"\\/var\\/cache\\/rpcbind\",\n" + "          \"gid\": 32,\n"
                + "          \"uid\": 32,\n" + "          \"gecos\": \"Rpcbind Daemon\",\n"
                + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n" + "        \"nobody\": {\n"
                + "          \"dir\": \"\\/\",\n" + "          \"gid\": 99,\n" + "          \"uid\": 99,\n"
                + "          \"gecos\": \"Nobody\",\n" + "          \"shell\": \"\\/sbin\\/nologin\"\n"
                + "        },\n" + "        \"sync\": {\n" + "          \"dir\": \"\\/sbin\",\n"
                + "          \"gid\": 0,\n" + "          \"uid\": 5,\n" + "          \"gecos\": \"sync\",\n"
                + "          \"shell\": \"\\/bin\\/sync\"\n" + "        },\n" + "        \"sshd\": {\n"
                + "          \"dir\": \"\\/var\\/empty\\/sshd\",\n" + "          \"gid\": 74,\n"
                + "          \"uid\": 74,\n" + "          \"gecos\": \"Privilege-separated SSH\",\n"
                + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n" + "        \"mail\": {\n"
                + "          \"dir\": \"\\/var\\/spool\\/mail\",\n" + "          \"gid\": 12,\n"
                + "          \"uid\": 8,\n" + "          \"gecos\": \"mail\",\n"
                + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n" + "        \"dbus\": {\n"
                + "          \"dir\": \"\\/\",\n" + "          \"gid\": 81,\n" + "          \"uid\": 81,\n"
                + "          \"gecos\": \"System message bus\",\n" + "          \"shell\": \"\\/sbin\\/nologin\"\n"
                + "        },\n" + "        \"root\": {\n" + "          \"dir\": \"\\/root\",\n"
                + "          \"gid\": 0,\n" + "          \"uid\": 0,\n" + "          \"gecos\": \"root\",\n"
                + "          \"shell\": \"\\/bin\\/bash\"\n" + "        },\n" + "        \"qpidd\": {\n"
                + "          \"dir\": \"\\/var\\/lib\\/qpidd\",\n" + "          \"gid\": 499,\n"
                + "          \"uid\": 498,\n" + "          \"gecos\": \"Owner of Qpidd Daemons\",\n"
                + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n" + "        \"ftp\": {\n"
                + "          \"dir\": \"\\/var\\/ftp\",\n" + "          \"gid\": 50,\n" + "          \"uid\": 14,\n"
                + "          \"gecos\": \"FTP User\",\n" + "          \"shell\": \"\\/sbin\\/nologin\"\n"
                + "        },\n" + "        \"adm\": {\n" + "          \"dir\": \"\\/var\\/adm\",\n"
                + "          \"gid\": 4,\n" + "          \"uid\": 3,\n" + "          \"gecos\": \"adm\",\n"
                + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n" + "        \"avahi\": {\n"
                + "          \"dir\": \"\\/var\\/run\\/avahi-daemon\",\n" + "          \"gid\": 70,\n"
                + "          \"uid\": 70,\n" + "          \"gecos\": \"Avahi mDNS\\/DNS-SD Stack\",\n"
                + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n" + "        \"rpcuser\": {\n"
                + "          \"dir\": \"\\/var\\/lib\\/nfs\",\n" + "          \"gid\": 29,\n"
                + "          \"uid\": 29,\n" + "          \"gecos\": \"RPC Service User\",\n"
                + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n" + "        \"operator\": {\n"
                + "          \"dir\": \"\\/root\",\n" + "          \"gid\": 0,\n" + "          \"uid\": 11,\n"
                + "          \"gecos\": \"operator\",\n" + "          \"shell\": \"\\/sbin\\/nologin\"\n"
                + "        },\n" + "        \"ntp\": {\n" + "          \"dir\": \"\\/etc\\/ntp\",\n"
                + "          \"gid\": 38,\n" + "          \"uid\": 38,\n" + "          \"gecos\": \"\",\n"
                + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n" + "        \"gdm\": {\n"
                + "          \"dir\": \"\\/var\\/lib\\/gdm\",\n" + "          \"gid\": 42,\n"
                + "          \"uid\": 42,\n" + "          \"gecos\": \"\",\n"
                + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        },\n" + "        \"postfix\": {\n"
                + "          \"dir\": \"\\/var\\/spool\\/postfix\",\n" + "          \"gid\": 89,\n"
                + "          \"uid\": 89,\n" + "          \"gecos\": \"\",\n"
                + "          \"shell\": \"\\/sbin\\/nologin\"\n" + "        }\n" + "      },\n"
                + "      \"group\": {\n" + "        \"bin\": {\n" + "          \"gid\": 1,\n"
                + "          \"members\": [\n" + "            \"root\",\n" + "            \"bin\",\n"
                + "            \"daemon\"\n" + "          ]\n" + "        },\n" + "        \"tty\": {\n"
                + "          \"gid\": 5,\n" + "          \"members\": [\n" + "            \n" + "          ]\n"
                + "        },\n" + "        \"dip\": {\n" + "          \"gid\": 40,\n" + "          \"members\": [\n"
                + "            \n" + "          ]\n" + "        },\n" + "        \"postdrop\": {\n"
                + "          \"gid\": 90,\n" + "          \"members\": [\n" + "            \n" + "          ]\n"
                + "        },\n" + "        \"uucp\": {\n" + "          \"gid\": 14,\n" + "          \"members\": [\n"
                + "            \"uucp\"\n" + "          ]\n" + "        },\n" + "        \"lock\": {\n"
                + "          \"gid\": 54,\n" + "          \"members\": [\n" + "            \n" + "          ]\n"
                + "        },\n" + "        \"haldaemon\": {\n" + "          \"gid\": 68,\n"
                + "          \"members\": [\n" + "            \"haldaemon\"\n" + "          ]\n" + "        },\n"
                + "        \"man\": {\n" + "          \"gid\": 15,\n" + "          \"members\": [\n" + "            \n"
                + "          ]\n" + "        },\n" + "        \"cgred\": {\n" + "          \"gid\": 496,\n"
                + "          \"members\": [\n" + "            \n" + "          ]\n" + "        },\n"
                + "        \"localadmin\": {\n" + "          \"gid\": 500,\n" + "          \"members\": [\n"
                + "            \n" + "          ]\n" + "        },\n" + "        \"gopher\": {\n"
                + "          \"gid\": 30,\n" + "          \"members\": [\n" + "            \n" + "          ]\n"
                + "        },\n" + "        \"vcsa\": {\n" + "          \"gid\": 69,\n" + "          \"members\": [\n"
                + "            \n" + "          ]\n" + "        },\n" + "        \"abrt\": {\n"
                + "          \"gid\": 173,\n" + "          \"members\": [\n" + "            \n" + "          ]\n"
                + "        },\n" + "        \"nfsnobody\": {\n" + "          \"gid\": 65534,\n"
                + "          \"members\": [\n" + "            \n" + "          ]\n" + "        },\n"
                + "        \"daemon\": {\n" + "          \"gid\": 2,\n" + "          \"members\": [\n"
                + "            \"root\",\n" + "            \"bin\",\n" + "            \"daemon\"\n" + "          ]\n"
                + "        },\n" + "        \"saslauth\": {\n" + "          \"gid\": 76,\n"
                + "          \"members\": [\n" + "            \n" + "          ]\n" + "        },\n"
                + "        \"lp\": {\n" + "          \"gid\": 7,\n" + "          \"members\": [\n"
                + "            \"daemon\",\n" + "            \"lp\"\n" + "          ]\n" + "        },\n"
                + "        \"sys\": {\n" + "          \"gid\": 3,\n" + "          \"members\": [\n"
                + "            \"root\",\n" + "            \"bin\",\n" + "            \"adm\"\n" + "          ]\n"
                + "        },\n" + "        \"tcpdump\": {\n" + "          \"gid\": 72,\n"
                + "          \"members\": [\n" + "            \n" + "          ]\n" + "        },\n"
                + "        \"games\": {\n" + "          \"gid\": 20,\n" + "          \"members\": [\n"
                + "            \n" + "          ]\n" + "        },\n" + "        \"audio\": {\n"
                + "          \"gid\": 63,\n" + "          \"members\": [\n" + "            \n" + "          ]\n"
                + "        },\n" + "        \"utmp\": {\n" + "          \"gid\": 22,\n" + "          \"members\": [\n"
                + "            \n" + "          ]\n" + "        },\n" + "        \"floppy\": {\n"
                + "          \"gid\": 19,\n" + "          \"members\": [\n" + "            \n" + "          ]\n"
                + "        },\n" + "        \"slocate\": {\n" + "          \"gid\": 21,\n"
                + "          \"members\": [\n" + "            \n" + "          ]\n" + "        },\n"
                + "        \"video\": {\n" + "          \"gid\": 39,\n" + "          \"members\": [\n"
                + "            \n" + "          ]\n" + "        },\n" + "        \"oprofile\": {\n"
                + "          \"gid\": 16,\n" + "          \"members\": [\n" + "            \n" + "          ]\n"
                + "        },\n" + "        \"desktop_admin_r\": {\n" + "          \"gid\": 495,\n"
                + "          \"members\": [\n" + "            \n" + "          ]\n" + "        },\n"
                + "        \"rpc\": {\n" + "          \"gid\": 32,\n" + "          \"members\": [\n" + "            \n"
                + "          ]\n" + "        },\n" + "        \"tape\": {\n" + "          \"gid\": 33,\n"
                + "          \"members\": [\n" + "            \n" + "          ]\n" + "        },\n"
                + "        \"nobody\": {\n" + "          \"gid\": 99,\n" + "          \"members\": [\n"
                + "            \n" + "          ]\n" + "        },\n" + "        \"utempter\": {\n"
                + "          \"gid\": 35,\n" + "          \"members\": [\n" + "            \n" + "          ]\n"
                + "        },\n" + "        \"sshd\": {\n" + "          \"gid\": 74,\n" + "          \"members\": [\n"
                + "            \n" + "          ]\n" + "        },\n" + "        \"desktop_user_r\": {\n"
                + "          \"gid\": 494,\n" + "          \"members\": [\n" + "            \n" + "          ]\n"
                + "        },\n" + "        \"mail\": {\n" + "          \"gid\": 12,\n" + "          \"members\": [\n"
                + "            \"mail\",\n" + "            \"postfix\"\n" + "          ]\n" + "        },\n"
                + "        \"stapdev\": {\n" + "          \"gid\": 498,\n" + "          \"members\": [\n"
                + "            \n" + "          ]\n" + "        },\n" + "        \"disk\": {\n"
                + "          \"gid\": 6,\n" + "          \"members\": [\n" + "            \"root\"\n" + "          ]\n"
                + "        },\n" + "        \"mem\": {\n" + "          \"gid\": 8,\n" + "          \"members\": [\n"
                + "            \n" + "          ]\n" + "        },\n" + "        \"dbus\": {\n"
                + "          \"gid\": 81,\n" + "          \"members\": [\n" + "            \n" + "          ]\n"
                + "        },\n" + "        \"stapusr\": {\n" + "          \"gid\": 497,\n"
                + "          \"members\": [\n" + "            \n" + "          ]\n" + "        },\n"
                + "        \"root\": {\n" + "          \"gid\": 0,\n" + "          \"members\": [\n"
                + "            \"root\"\n" + "          ]\n" + "        },\n" + "        \"qpidd\": {\n"
                + "          \"gid\": 499,\n" + "          \"members\": [\n" + "            \n" + "          ]\n"
                + "        },\n" + "        \"dialout\": {\n" + "          \"gid\": 18,\n"
                + "          \"members\": [\n" + "            \n" + "          ]\n" + "        },\n"
                + "        \"ftp\": {\n" + "          \"gid\": 50,\n" + "          \"members\": [\n" + "            \n"
                + "          ]\n" + "        },\n" + "        \"adm\": {\n" + "          \"gid\": 4,\n"
                + "          \"members\": [\n" + "            \"root\",\n" + "            \"adm\",\n"
                + "            \"daemon\"\n" + "          ]\n" + "        },\n" + "        \"avahi\": {\n"
                + "          \"gid\": 70,\n" + "          \"members\": [\n" + "            \n" + "          ]\n"
                + "        },\n" + "        \"cdrom\": {\n" + "          \"gid\": 11,\n" + "          \"members\": [\n"
                + "            \n" + "          ]\n" + "        },\n" + "        \"rpcuser\": {\n"
                + "          \"gid\": 29,\n" + "          \"members\": [\n" + "            \n" + "          ]\n"
                + "        },\n" + "        \"ntp\": {\n" + "          \"gid\": 38,\n" + "          \"members\": [\n"
                + "            \n" + "          ]\n" + "        },\n" + "        \"gdm\": {\n"
                + "          \"gid\": 42,\n" + "          \"members\": [\n" + "            \n" + "          ]\n"
                + "        },\n" + "        \"wheel\": {\n" + "          \"gid\": 10,\n" + "          \"members\": [\n"
                + "            \"root\"\n" + "          ]\n" + "        },\n" + "        \"kmem\": {\n"
                + "          \"gid\": 9,\n" + "          \"members\": [\n" + "            \n" + "          ]\n"
                + "        },\n" + "        \"users\": {\n" + "          \"gid\": 100,\n"
                + "          \"members\": [\n" + "            \n" + "          ]\n" + "        },\n"
                + "        \"postfix\": {\n" + "          \"gid\": 89,\n" + "          \"members\": [\n"
                + "            \n" + "          ]\n" + "        }\n" + "      }\n" + "    },\n"
                + "    \"macaddress\": \"FA:16:3E:5C:CE:D3\",\n" + "    \"recipes\": [\n" + "      \n" + "    ],\n"
                + "    \"os_version\": \"2.6.32-220.el6.x86_64\",\n" + "    \"uptime_seconds\": 655,\n"
                + "    \"chef_packages\": {\n" + "      \"chef\": {\n" + "        \"version\": \"11.4.0\",\n"
                + "        \"chef_root\": \"\\/usr\\/lib64\\/ruby\\/gems\\/1.8\\/gems\\/chef-11.4.0\\/lib\"\n"
                + "      },\n" + "      \"ohai\": {\n" + "        \"version\": \"6.16.0\",\n"
                + "        \"ohai_root\": \"\\/usr\\/lib64\\/ruby\\/gems\\/1.8\\/gems\\/ohai-6.16.0\\/lib\\/ohai\"\n"
                + "      }\n" + "    },\n" + "    \"counters\": {\n" + "      \"network\": {\n"
                + "        \"interfaces\": {\n" + "          \"eth0\": {\n" + "            \"rx\": {\n"
                + "              \"drop\": \"0\",\n" + "              \"bytes\": \"245927\",\n"
                + "              \"overrun\": \"0\",\n" + "              \"packets\": \"2172\",\n"
                + "              \"errors\": \"0\"\n" + "            },\n" + "            \"tx\": {\n"
                + "              \"queuelen\": \"1000\",\n" + "              \"drop\": \"0\",\n"
                + "              \"collisions\": \"0\",\n" + "              \"bytes\": \"43274\",\n"
                + "              \"carrier\": \"0\",\n" + "              \"packets\": \"146\",\n"
                + "              \"errors\": \"0\"\n" + "            }\n" + "          },\n" + "          \"lo\": {\n"
                + "            \"rx\": {\n" + "              \"drop\": \"0\",\n"
                + "              \"bytes\": \"328\",\n" + "              \"overrun\": \"0\",\n"
                + "              \"packets\": \"6\",\n" + "              \"errors\": \"0\"\n" + "            },\n"
                + "            \"tx\": {\n" + "              \"drop\": \"0\",\n"
                + "              \"collisions\": \"0\",\n" + "              \"bytes\": \"328\",\n"
                + "              \"carrier\": \"0\",\n" + "              \"packets\": \"6\",\n"
                + "              \"errors\": \"0\"\n" + "            }\n" + "          }\n" + "        }\n"
                + "      }\n" + "    },\n" + "    \"ipaddress\": \"172.30.5.13\"\n" + "  },\n"
                + "  \"name\": \"sdc15102013d.novalocal\",\n" + "  \"default\": {\n" + "    \n" + "  }\n" + "}";

        // when
        when(clientConfig.getClient()).thenReturn(client);
        when(client.target(anyString())).thenReturn(webResource);
        when(webResource.request(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(openStackRegion.getChefServerEndPoint()).thenReturn("http://url");
        when(builder.get()).thenReturn(response);
        when(response.readEntity(String.class)).thenReturn(responseString);

        ChefNode createdChefNode = chefNodeDaoRestImpl.loadNode(chefNodeName, "token");

        // then
        assertNotNull(createdChefNode);
        verify(builder).get();
    }

    /**
     * Testing deleteNode.
     * 
     * @throws CanNotCallChefException
     * @throws EntityNotFoundException
     */
    @Test
    public void shouldDeleteNode() throws CanNotCallChefException, EntityNotFoundException {
        ChefNodeDaoRestImpl chefNodeDaoRestImpl = new ChefNodeDaoRestImpl();
        SystemPropertiesProvider propertiesProvider = mock(SystemPropertiesProvider.class);
        MixlibAuthenticationDigester mixlibAuthenticationDigester = mock(MixlibAuthenticationDigester.class);
        ChefClientConfig clientConfig = mock(ChefClientConfig.class);
        OpenStackRegion openStackRegion = mock(OpenStackRegion.class);

        Client client = mock(Client.class);

        ChefNode chefNode = new ChefNode();
        chefNode.setName("hostname.domain");

        chefNodeDaoRestImpl.setPropertiesProvider(propertiesProvider);
        chefNodeDaoRestImpl.setDigester(mixlibAuthenticationDigester);
        chefNodeDaoRestImpl.setClientConfig(clientConfig);
        WebTarget webResource = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        chefNodeDaoRestImpl.setOpenStackRegion(openStackRegion);

        // when
        when(propertiesProvider.getProperty(anyString())).thenReturn("http://localhost");

        when(clientConfig.getClient()).thenReturn(client);
        when(client.target(anyString())).thenReturn(webResource);
        when(webResource.request(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.header(anyString(), anyObject())).thenReturn(builder);
        chefNodeDaoRestImpl.setOpenStackRegion(openStackRegion);

        // then
        chefNodeDaoRestImpl.deleteNode(chefNode, "token");

        verify(client, times(1)).target(anyString());
        verify(propertiesProvider, atLeastOnce()).getProperty(anyString());

    }

    /**
     * Testing UpdteNode.
     * 
     * @throws CanNotCallChefException
     * @throws EntityNotFoundException
     */
    @Test
    public void shouldUpdateNode() throws CanNotCallChefException, EntityNotFoundException, OpenStackException {
        ChefNodeDaoRestImpl chefNodeDaoRestImpl = new ChefNodeDaoRestImpl();
        SystemPropertiesProvider propertiesProvider = mock(SystemPropertiesProvider.class);
        MixlibAuthenticationDigester mixlibAuthenticationDigester = mock(MixlibAuthenticationDigester.class);
        Client client = mock(Client.class);
        OpenStackRegion openStackRegion = mock(OpenStackRegion.class);
        ChefClientConfig clientConfig = mock(ChefClientConfig.class);
        chefNodeDaoRestImpl.setOpenStackRegion(openStackRegion);

        ChefNode chefNode = mock(ChefNode.class);
        String payload = "{\"json_class\":\"Chef::Node\",\"override\":{},\"chef_environment\":\"_default\",\"chef_type\":\"node\",\"normal\":{\"tags\":[]},\"run_list\":[],\"automatic\":{\"os\":\"linux\",\"cpu\":{\"real\":0,\"0\":{\"model\":\"2\",\"flags\":[\"fpu\",\"de\",\"pse\",\"tsc\",\"msr\",\"pae\",\"mce\",\"cx8\",\"apic\",\"mtrr\",\"pge\",\"mca\",\"cmov\",\"pse36\",\"clflush\",\"mmx\",\"fxsr\",\"sse\",\"sse2\",\"syscall\",\"nx\",\"lm\",\"up\",\"rep_good\",\"unfair_spinlock\",\"pni\",\"vmx\",\"cx16\",\"popcnt\",\"hypervisor\",\"lahf_lm\"],\"mhz\":\"2394.230\",\"model_name\":\"QEMU Virtual CPU version 1.0\",\"family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"stepping\":\"3\",\"cache_size\":\"4096 KB\"},\"total\":1},\"current_user\":\"root\",\"ip6address\":\"fe80::f816:3eff:fe5c:ced3\",\"virtualization\":{\"role\":\"guest\",\"system\":\"kvm\"},\"languages\":{\"ruby\":{\"bin_dir\":\"/usr/bin\",\"host_os\":\"linux-gnu\",\"target_os\":\"linux\",\"host\":\"x86_64-redhat-linux-gnu\",\"host_cpu\":\"x86_64\",\"version\":\"1.8.7\",\"host_vendor\":\"redhat\",\"target\":\"x86_64-redhat-linux-gnu\",\"gem_bin\":\"/usr/bin/gem\",\"platform\":\"x86_64-linux\",\"release_date\":\"2011-06-30\",\"ruby_bin\":\"/usr/bin/ruby\",\"target_cpu\":\"x86_64\",\"gems_dir\":\"/usr/lib64/ruby/gems/1.8\",\"target_vendor\":\"redhat\"},\"perl\":{\"version\":\"5.10.1\",\"archname\":\"x86_64-linux-thread-multi\"},\"python\":{\"builddate\":\"Dec 7 2011, 20:48:22\",\"version\":\"2.6.6\"},\"lua\":{\"version\":\"5.1.4\"},\"java\":{\"runtime\":{\"name\":\"OpenJDK Runtime Environment (IcedTea6 1.10.4)\",\"build\":\"rhel-1.41.1.10.4.el6-x86_64\"},\"version\":\"1.6.0_22\",\"hotspot\":{\"name\":\"OpenJDK 64-Bit Server VM\",\"build\":\"20.0-b11, mixed mode\"}}},\"lsb\":{\"release\":\"6.2\",\"id\":\"CentOS\",\"description\":\"CentOS release 6.2 (Final)\",\"codename\":\"Final\"},\"domain\":\"novalocal\",\"platform_version\":\"6.2\",\"platform_family\":\"rhel\",\"roles\":[],\"memory\":{\"writeback\":\"0kB\",\"inactive\":\"103500kB\",\"vmalloc_chunk\":\"34359731080kB\",\"dirty\":\"116kB\",\"vmalloc_used\":\"3528kB\",\"page_tables\":\"3984kB\",\"committed_as\":\"193392kB\",\"free\":\"286480kB\",\"slab\":\"27276kB\",\"nfs_unstable\":\"0kB\",\"anon_pages\":\"112640kB\",\"active\":\"70288kB\",\"swap\":{\"free\":\"0kB\",\"cached\":\"0kB\",\"total\":\"0kB\"},\"buffers\":\"11468kB\",\"bounce\":\"0kB\",\"commit_limit\":\"251360kB\",\"vmalloc_total\":\"34359738367kB\",\"mapped\":\"12436kB\",\"slab_reclaimable\":\"7728kB\",\"cached\":\"49652kB\",\"total\":\"502720kB\",\"slab_unreclaim\":\"19548kB\"},\"idletime\":\"10 minutes 11 seconds\",\"command\":{\"ps\":\"ps -ef\"},\"idletime_seconds\":611,\"ohai_time\":2381841405.78531,\"uptime\":\"10 minutes 55 seconds\",\"dmi\":{\"chassis\":{\"lock\":\"Not Present\",\"type\":\"Other\",\"asset_tag\":\"Not Specified\",\"version\":\"Not Specified\",\"power_supply_state\":\"Safe\",\"security_status\":\"Unknown\",\"boot_up_state\":\"Safe\",\"height\":\"Unspecified\",\"all_records\":[{\"Lock\":\"Not Present\",\"size\":\"3\",\"Serial Number\":\"Not Specified\",\"Version\":\"Not Specified\",\"Boot-up State\":\"Safe\",\"Asset Tag\":\"Not Specified\",\"Type\":\"Other\",\"Security Status\":\"Unknown\",\"Number Of Power Cords\":\"Unspecified\",\"application_identifier\":\"Chassis Information\",\"Power Supply State\":\"Safe\",\"record_id\":\"0x0300\",\"OEM Information\":\"0x00000000\",\"Height\":\"Unspecified\",\"Thermal State\":\"Safe\",\"Manufacturer\":\"Bochs\"}],\"serial_number\":\"Not Specified\",\"manufacturer\":\"Bochs\",\"number_of_power_cords\":\"Unspecified\",\"oem_information\":\"0x00000000\",\"thermal_state\":\"Safe\"},\"structures\":{\"size\":\"263\",\"count\":\"10\"},\"dmidecode_version\":\"2.11\",\"processor\":{\"locator\":\"DIMM 0\",\"status\":\"No errors detected\",\"bank_locator\":\"Not Specified\",\"voltage\":\"Unknown\",\"max_speed\":\"2000 MHz\",\"size\":\"512 MB\",\"l2_cache_handle\":\"Not Provided\",\"form_factor\":\"DIMM\",\"data_width\":\"64 bits\",\"type\":\"RAM\",\"range_size\":\"512 MB\",\"total_width\":\"64 bits\",\"physical_device_handle\":\"0x1100\",\"number_of_devices\":\"1\",\"id\":\"23 06 00 00 FD FB 8B 07\",\"maximum_capacity\":\"512 MB\",\"partition_width\":\"1\",\"type_detail\":\"None\",\"l1_cache_handle\":\"Not Provided\",\"partition_row_position\":\"1\",\"physical_array_handle\":\"0x1000\",\"version\":\"Not Specified\",\"use\":\"System Memory\",\"error_correction_type\":\"Multi-bit ECC\",\"starting_address\":\"0x00000000000\",\"all_records\":[{\"Array Handle\":\"0x1000\",\"L1 Cache Handle\":\"Not Provided\",\"Voltage\":\"Unknown\",\"Locator\":\"DIMM 0\",\"ID\":\"23 06 00 00 FD FB 8B 07\",\"size\":\"4\",\"Set\":\"None\",\"Size\":\"512 MB\",\"Number Of Devices\":\"1\",\"Version\":\"Not Specified\",\"Status\":\"No errors detected\",\"Maximum Capacity\":\"512 MB\",\"Partition Width\":\"1\",\"Partition Row Position\":\"1\",\"Type Detail\":\"None\",\"External Clock\":\"Unknown\",\"Memory Array Mapped Address Handle\":\"0x1300\",\"Bank Locator\":\"Not Specified\",\"Total Width\":\"64 bits\",\"Current Speed\":\"2000 MHz\",\"Type\":\"RAM\",\"Data Width\":\"64 bits\",\"L3 Cache Handle\":\"Not Provided\",\"Socket Designation\":\"CPU 1\",\"Max Speed\":\"2000 MHz\",\"Range Size\":\"512 MB\",\"Form Factor\":\"DIMM\",\"Physical Array Handle\":\"0x1000\",\"Use\":\"System Memory\",\"application_identifier\":\"End Of Table\",\"Starting Address\":\"0x00000000000\",\"Ending Address\":\"0x0001FFFFFFF\",\"Physical Device Handle\":\"0x1100\",\"Upgrade\":\"Other\",\"record_id\":\"0x0401\",\"Error Information Handle\":\"0x0307\",\"Manufacturer\":\"Bochs\",\"L2 Cache Handle\":\"Not Provided\",\"Error Correction Type\":\"Multi-bit ECC\",\"Family\":\"Other\",\"Location\":\"Other\"}],\"current_speed\":\"2000 MHz\",\"location\":\"Other\",\"socket_designation\":\"CPU 1\",\"manufacturer\":\"Bochs\",\"family\":\"Other\",\"set\":\"None\",\"memory_array_mapped_address_handle\":\"0x1300\",\"external_clock\":\"Unknown\",\"array_handle\":\"0x1000\",\"upgrade\":\"Other\",\"l3_cache_handle\":\"Not Provided\",\"ending_address\":\"0x0001FFFFFFF\",\"error_information_handle\":\"0x0307\"},\"smbios_version\":\"2.4\",\"bios\":{\"version\":\"Bochs\",\"bios_revision\":\"1.0\",\"all_records\":[{\"size\":\"0\",\"Characteristics\":{\"BIOS characteristics not supported\":null,\"Targeted content distribution is supported\":null},\"Release Date\":\"01/01/2007\",\"Runtime Size\":\"96 kB\",\"Version\":\"Bochs\",\"BIOS Revision\":\"1.0\",\"application_identifier\":\"BIOS Information\",\"record_id\":\"0x0000\",\"ROM Size\":\"64 kB\",\"Vendor\":\"Bochs\",\"Address\":\"0xE8000\"}],\"runtime_size\":\"96 kB\",\"release_date\":\"01/01/2007\",\"address\":\"0xE8000\",\"rom_size\":\"64 kB\",\"vendor\":\"Bochs\"},\"system\":{\"wake_up_type\":\"Power Switch\",\"version\":\"Not Specified\",\"product_name\":\"Bochs\",\"all_records\":[{\"Wake-up Type\":\"Power Switch\",\"Product Name\":\"Bochs\",\"size\":\"1\",\"Serial Number\":\"Not Specified\",\"Version\":\"Not Specified\",\"UUID\":\"A378FD62-BBC8-4BA4-8C8D-8D196789708C\",\"SKU Number\":\"Not Specified\",\"application_identifier\":\"System Information\",\"record_id\":\"0x0100\",\"Manufacturer\":\"Bochs\",\"Family\":\"Not Specified\"}],\"serial_number\":\"Not Specified\",\"manufacturer\":\"Bochs\",\"family\":\"Not Specified\",\"sku_number\":\"Not Specified\",\"uuid\":\"A378FD62-BBC8-4BA4-8C8D-8D196789708C\"}},\"block_device\":{\"ram0\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram1\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram2\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram3\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram4\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram5\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram6\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram7\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram8\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram9\":{\"size\":\"32768\",\"removable\":\"0\"},\"vda\":{\"size\":\"10485760\",\"vendor\":\"6900\",\"removable\":\"0\"},\"loop0\":{\"size\":\"0\",\"removable\":\"0\"},\"loop1\":{\"size\":\"0\",\"removable\":\"0\"},\"loop2\":{\"size\":\"0\",\"removable\":\"0\"},\"loop3\":{\"size\":\"0\",\"removable\":\"0\"},\"loop4\":{\"size\":\"0\",\"removable\":\"0\"},\"loop5\":{\"size\":\"0\",\"removable\":\"0\"},\"loop6\":{\"size\":\"0\",\"removable\":\"0\"},\"loop7\":{\"size\":\"0\",\"removable\":\"0\"},\"ram10\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram11\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram12\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram13\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram14\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram15\":{\"size\":\"32768\",\"removable\":\"0\"}},\"platform\":\"centos\",\"fqdn\":\"sdc15102013d.novalocal\",\"filesystem\":{\"/sys\":{\"mount\":\"/sys\",\"fs_type\":\"sysfs\",\"mount_options\":[\"rw\",\"relatime\"]},\"/dev/vda1\":{\"mount\":\"/\",\"fs_type\":\"ext4\",\"percent_used\":\"44%\",\"kb_size\":\"5159552\",\"mount_options\":[\"rw\"],\"uuid\":\"93ab5295-5527-4919-8098-ce9b49bc7a65\",\"kb_used\":\"2142816\",\"kb_available\":\"2754648\"},\"/proc/bus/usb\":{\"mount\":\"/proc/bus/usb\",\"fs_type\":\"usbfs\",\"mount_options\":[\"rw\",\"relatime\"]},\"tmpfs\":{\"mount\":\"/dev/shm\",\"fs_type\":\"tmpfs\",\"percent_used\":\"0%\",\"kb_size\":\"251360\",\"mount_options\":[\"rw\"],\"kb_used\":\"0\",\"kb_available\":\"251360\"},\"rootfs\":{\"mount\":\"/\",\"fs_type\":\"rootfs\",\"mount_options\":[\"rw\"]},\"sysfs\":{\"mount\":\"/sys\",\"fs_type\":\"sysfs\",\"mount_options\":[\"rw\"]},\"/proc\":{\"mount\":\"/proc\",\"fs_type\":\"proc\",\"mount_options\":[\"rw\",\"relatime\"]},\"devpts\":{\"mount\":\"/dev/pts\",\"fs_type\":\"devpts\",\"mount_options\":[\"rw\",\"gid=5\",\"mode=620\"]},\"/etc/auto.misc\":{\"mount\":\"/misc\",\"fs_type\":\"autofs\",\"mount_options\":[\"rw\",\"relatime\",\"fd=7\",\"pgrp=1402\",\"timeout=300\",\"minproto=5\",\"maxproto=5\",\"indirect\"]},\"none\":{\"mount\":\"/proc/sys/fs/binfmt_misc\",\"fs_type\":\"binfmt_misc\",\"mount_options\":[\"rw\"]},\"udev\":{\"mount\":\"/dev\",\"fs_type\":\"devtmpfs\",\"mount_options\":[\"rw\",\"relatime\",\"size=241740k\",\"nr_inodes=60435\",\"mode=755\"]},\"proc\":{\"mount\":\"/proc\",\"fs_type\":\"proc\",\"mount_options\":[\"rw\"]},\"sunrpc\":{\"mount\":\"/var/lib/nfs/rpc_pipefs\",\"fs_type\":\"rpc_pipefs\",\"mount_options\":[\"rw\"]},\"-hosts\":{\"mount\":\"/net\",\"fs_type\":\"autofs\",\"mount_options\":[\"rw\",\"relatime\",\"fd=13\",\"pgrp=1402\",\"timeout=300\",\"minproto=5\",\"maxproto=5\",\"indirect\"]}},\"kernel\":{\"release\":\"2.6.32-220.el6.x86_64\",\"os\":\"GNU/Linux\",\"machine\":\"x86_64\",\"version\":\"#1 SMP Tue Dec 6 19:48:22 GMT 2011\",\"modules\":{\"virtio_balloon\":{\"size\":\"4347\",\"refcount\":\"0\"},\"microcode\":{\"size\":\"112594\",\"refcount\":\"0\"},\"i2c_core\":{\"size\":\"31276\",\"refcount\":\"1\"},\"mbcache\":{\"size\":\"8144\",\"refcount\":\"1\"},\"ip6table_filter\":{\"size\":\"2889\",\"refcount\":\"1\"},\"i2c_piix4\":{\"size\":\"12608\",\"refcount\":\"0\"},\"ip6t_REJECT\":{\"size\":\"4628\",\"refcount\":\"2\"},\"ata_generic\":{\"size\":\"3837\",\"refcount\":\"0\"},\"virtio_pci\":{\"size\":\"6687\",\"refcount\":\"0\"},\"dm_log\":{\"size\":\"10122\",\"refcount\":\"2\"},\"dm_mod\":{\"size\":\"81500\",\"refcount\":\"2\"},\"nf_conntrack\":{\"size\":\"79453\",\"refcount\":\"2\"},\"ipv6\":{\"size\":\"322029\",\"refcount\":\"29\"},\"virtio_net\":{\"size\":\"15839\",\"refcount\":\"0\"},\"ata_piix\":{\"size\":\"22846\",\"refcount\":\"0\"},\"ip6_tables\":{\"size\":\"19458\",\"refcount\":\"1\"},\"xt_state\":{\"size\":\"1492\",\"refcount\":\"2\"},\"virtio_blk\":{\"size\":\"6671\",\"refcount\":\"2\"},\"nf_conntrack_ipv6\":{\"size\":\"8748\",\"refcount\":\"2\"},\"dm_mirror\":{\"size\":\"14101\",\"refcount\":\"0\"},\"autofs4\":{\"size\":\"26888\",\"refcount\":\"3\"},\"pata_acpi\":{\"size\":\"3701\",\"refcount\":\"0\"},\"dm_region_hash\":{\"size\":\"12170\",\"refcount\":\"1\"},\"nf_defrag_ipv6\":{\"size\":\"12182\",\"refcount\":\"1\"},\"ext4\":{\"size\":\"364410\",\"refcount\":\"1\"},\"virtio_ring\":{\"size\":\"7729\",\"refcount\":\"4\"},\"jbd2\":{\"size\":\"88738\",\"refcount\":\"1\"},\"sunrpc\":{\"size\":\"243758\",\"refcount\":\"1\"},\"virtio\":{\"size\":\"4890\",\"refcount\":\"4\"}},\"name\":\"Linux\"},\"hostname\":\"sdc15102013d\",\"network\":{\"default_interface\":\"eth0\",\"interfaces\":{\"eth0\":{\"type\":\"eth\",\"addresses\":{\"172.30.5.13\":{\"scope\":\"Global\",\"netmask\":\"255.255.255.0\",\"family\":\"inet\",\"broadcast\":\"172.30.5.255\",\"prefixlen\":\"24\"},\"fe80::f816:3eff:fe5c:ced3\":{\"scope\":\"Link\",\"family\":\"inet6\",\"prefixlen\":\"64\"},\"FA:16:3E:5C:CE:D3\":{\"family\":\"lladdr\"}},\"arp\":{\"172.30.5.1\":\"54:52:00:37:68:64\"},\"flags\":[\"BROADCAST\",\"MULTICAST\",\"UP\",\"LOWER_UP\"],\"number\":\"0\",\"routes\":[{\"scope\":\"link\",\"destination\":\"172.30.5.0/24\",\"proto\":\"kernel\",\"src\":\"172.30.5.13\",\"family\":\"inet\"},{\"scope\":\"link\",\"destination\":\"169.254.0.0/16\",\"family\":\"inet\",\"metric\":\"1002\"},{\"via\":\"172.30.5.1\",\"destination\":\"default\",\"family\":\"inet\"},{\"destination\":\"fe80::/64\",\"proto\":\"kernel\",\"family\":\"inet6\",\"metric\":\"256\"}],\"encapsulation\":\"Ethernet\",\"mtu\":\"1500\",\"state\":\"up\"},\"lo\":{\"addresses\":{\"::1\":{\"scope\":\"Node\",\"family\":\"inet6\",\"prefixlen\":\"128\"},\"127.0.0.1\":{\"scope\":\"Node\",\"netmask\":\"255.0.0.0\",\"family\":\"inet\",\"prefixlen\":\"8\"}},\"flags\":[\"LOOPBACK\",\"UP\",\"LOWER_UP\"],\"routes\":[{\"destination\":\"unreachable\",\"family\":\"inet6\",\"metric\":\"1024\"}],\"encapsulation\":\"Loopback\",\"mtu\":\"16436\",\"state\":\"unknown\"}},\"default_gateway\":\"172.30.5.1\"},\"keys\":{\"ssh\":{}},\"etc\":{\"passwd\":{\"bin\":{\"dir\":\"/bin\",\"gid\":1,\"uid\":1,\"gecos\":\"bin\",\"shell\":\"/sbin/nologin\"},\"uucp\":{\"dir\":\"/var/spool/uucp\",\"gid\":14,\"uid\":10,\"gecos\":\"uucp\",\"shell\":\"/sbin/nologin\"},\"haldaemon\":{\"dir\":\"/\",\"gid\":68,\"uid\":68,\"gecos\":\"HAL daemon\",\"shell\":\"/sbin/nologin\"},\"localadmin\":{\"dir\":\"/home/localadmin\",\"gid\":500,\"uid\":500,\"gecos\":\"\",\"shell\":\"/bin/bash\"},\"gopher\":{\"dir\":\"/var/gopher\",\"gid\":30,\"uid\":13,\"gecos\":\"gopher\",\"shell\":\"/sbin/nologin\"},\"vcsa\":{\"dir\":\"/dev\",\"gid\":69,\"uid\":69,\"gecos\":\"virtual console memory owner\",\"shell\":\"/sbin/nologin\"},\"abrt\":{\"dir\":\"/etc/abrt\",\"gid\":173,\"uid\":173,\"gecos\":\"\",\"shell\":\"/sbin/nologin\"},\"nfsnobody\":{\"dir\":\"/var/lib/nfs\",\"gid\":65534,\"uid\":65534,\"gecos\":\"Anonymous NFS User\",\"shell\":\"/sbin/nologin\"},\"daemon\":{\"dir\":\"/sbin\",\"gid\":2,\"uid\":2,\"gecos\":\"daemon\",\"shell\":\"/sbin/nologin\"},\"saslauth\":{\"dir\":\"/var/empty/saslauth\",\"gid\":76,\"uid\":499,\"gecos\":\"\\\"Saslauthd user\\\"\",\"shell\":\"/sbin/nologin\"},\"lp\":{\"dir\":\"/var/spool/lpd\",\"gid\":7,\"uid\":4,\"gecos\":\"lp\",\"shell\":\"/sbin/nologin\"},\"tcpdump\":{\"dir\":\"/\",\"gid\":72,\"uid\":72,\"gecos\":\"\",\"shell\":\"/sbin/nologin\"},\"games\":{\"dir\":\"/usr/games\",\"gid\":100,\"uid\":12,\"gecos\":\"games\",\"shell\":\"/sbin/nologin\"},\"halt\":{\"dir\":\"/sbin\",\"gid\":0,\"uid\":7,\"gecos\":\"halt\",\"shell\":\"/sbin/halt\"},\"shutdown\":{\"dir\":\"/sbin\",\"gid\":0,\"uid\":6,\"gecos\":\"shutdown\",\"shell\":\"/sbin/shutdown\"},\"oprofile\":{\"dir\":\"/home/oprofile\",\"gid\":16,\"uid\":16,\"gecos\":\"Special user account to be used by OProfile\",\"shell\":\"/sbin/nologin\"},\"rpc\":{\"dir\":\"/var/cache/rpcbind\",\"gid\":32,\"uid\":32,\"gecos\":\"Rpcbind Daemon\",\"shell\":\"/sbin/nologin\"},\"nobody\":{\"dir\":\"/\",\"gid\":99,\"uid\":99,\"gecos\":\"Nobody\",\"shell\":\"/sbin/nologin\"},\"sync\":{\"dir\":\"/sbin\",\"gid\":0,\"uid\":5,\"gecos\":\"sync\",\"shell\":\"/bin/sync\"},\"sshd\":{\"dir\":\"/var/empty/sshd\",\"gid\":74,\"uid\":74,\"gecos\":\"Privilege-separated SSH\",\"shell\":\"/sbin/nologin\"},\"mail\":{\"dir\":\"/var/spool/mail\",\"gid\":12,\"uid\":8,\"gecos\":\"mail\",\"shell\":\"/sbin/nologin\"},\"dbus\":{\"dir\":\"/\",\"gid\":81,\"uid\":81,\"gecos\":\"System message bus\",\"shell\":\"/sbin/nologin\"},\"root\":{\"dir\":\"/root\",\"gid\":0,\"uid\":0,\"gecos\":\"root\",\"shell\":\"/bin/bash\"},\"qpidd\":{\"dir\":\"/var/lib/qpidd\",\"gid\":499,\"uid\":498,\"gecos\":\"Owner of Qpidd Daemons\",\"shell\":\"/sbin/nologin\"},\"ftp\":{\"dir\":\"/var/ftp\",\"gid\":50,\"uid\":14,\"gecos\":\"FTP User\",\"shell\":\"/sbin/nologin\"},\"adm\":{\"dir\":\"/var/adm\",\"gid\":4,\"uid\":3,\"gecos\":\"adm\",\"shell\":\"/sbin/nologin\"},\"avahi\":{\"dir\":\"/var/run/avahi-daemon\",\"gid\":70,\"uid\":70,\"gecos\":\"Avahi mDNS/DNS-SD Stack\",\"shell\":\"/sbin/nologin\"},\"rpcuser\":{\"dir\":\"/var/lib/nfs\",\"gid\":29,\"uid\":29,\"gecos\":\"RPC Service User\",\"shell\":\"/sbin/nologin\"},\"operator\":{\"dir\":\"/root\",\"gid\":0,\"uid\":11,\"gecos\":\"operator\",\"shell\":\"/sbin/nologin\"},\"ntp\":{\"dir\":\"/etc/ntp\",\"gid\":38,\"uid\":38,\"gecos\":\"\",\"shell\":\"/sbin/nologin\"},\"gdm\":{\"dir\":\"/var/lib/gdm\",\"gid\":42,\"uid\":42,\"gecos\":\"\",\"shell\":\"/sbin/nologin\"},\"postfix\":{\"dir\":\"/var/spool/postfix\",\"gid\":89,\"uid\":89,\"gecos\":\"\",\"shell\":\"/sbin/nologin\"}},\"group\":{\"bin\":{\"gid\":1,\"members\":[\"root\",\"bin\",\"daemon\"]},\"tty\":{\"gid\":5,\"members\":[]},\"dip\":{\"gid\":40,\"members\":[]},\"postdrop\":{\"gid\":90,\"members\":[]},\"uucp\":{\"gid\":14,\"members\":[\"uucp\"]},\"lock\":{\"gid\":54,\"members\":[]},\"haldaemon\":{\"gid\":68,\"members\":[\"haldaemon\"]},\"man\":{\"gid\":15,\"members\":[]},\"cgred\":{\"gid\":496,\"members\":[]},\"localadmin\":{\"gid\":500,\"members\":[]},\"gopher\":{\"gid\":30,\"members\":[]},\"vcsa\":{\"gid\":69,\"members\":[]},\"abrt\":{\"gid\":173,\"members\":[]},\"nfsnobody\":{\"gid\":65534,\"members\":[]},\"daemon\":{\"gid\":2,\"members\":[\"root\",\"bin\",\"daemon\"]},\"saslauth\":{\"gid\":76,\"members\":[]},\"lp\":{\"gid\":7,\"members\":[\"daemon\",\"lp\"]},\"sys\":{\"gid\":3,\"members\":[\"root\",\"bin\",\"adm\"]},\"tcpdump\":{\"gid\":72,\"members\":[]},\"games\":{\"gid\":20,\"members\":[]},\"audio\":{\"gid\":63,\"members\":[]},\"utmp\":{\"gid\":22,\"members\":[]},\"floppy\":{\"gid\":19,\"members\":[]},\"slocate\":{\"gid\":21,\"members\":[]},\"video\":{\"gid\":39,\"members\":[]},\"oprofile\":{\"gid\":16,\"members\":[]},\"desktop_admin_r\":{\"gid\":495,\"members\":[]},\"rpc\":{\"gid\":32,\"members\":[]},\"tape\":{\"gid\":33,\"members\":[]},\"nobody\":{\"gid\":99,\"members\":[]},\"utempter\":{\"gid\":35,\"members\":[]},\"sshd\":{\"gid\":74,\"members\":[]},\"desktop_user_r\":{\"gid\":494,\"members\":[]},\"mail\":{\"gid\":12,\"members\":[\"mail\",\"postfix\"]},\"stapdev\":{\"gid\":498,\"members\":[]},\"disk\":{\"gid\":6,\"members\":[\"root\"]},\"mem\":{\"gid\":8,\"members\":[]},\"dbus\":{\"gid\":81,\"members\":[]},\"stapusr\":{\"gid\":497,\"members\":[]},\"root\":{\"gid\":0,\"members\":[\"root\"]},\"qpidd\":{\"gid\":499,\"members\":[]},\"dialout\":{\"gid\":18,\"members\":[]},\"ftp\":{\"gid\":50,\"members\":[]},\"adm\":{\"gid\":4,\"members\":[\"root\",\"adm\",\"daemon\"]},\"avahi\":{\"gid\":70,\"members\":[]},\"cdrom\":{\"gid\":11,\"members\":[]},\"rpcuser\":{\"gid\":29,\"members\":[]},\"ntp\":{\"gid\":38,\"members\":[]},\"gdm\":{\"gid\":42,\"members\":[]},\"wheel\":{\"gid\":10,\"members\":[\"root\"]},\"kmem\":{\"gid\":9,\"members\":[]},\"users\":{\"gid\":100,\"members\":[]},\"postfix\":{\"gid\":89,\"members\":[]}}},\"macaddress\":\"FA:16:3E:5C:CE:D3\",\"recipes\":[],\"os_version\":\"2.6.32-220.el6.x86_64\",\"uptime_seconds\":655,\"chef_packages\":{\"chef\":{\"version\":\"11.4.0\",\"chef_root\":\"/usr/lib64/ruby/gems/1.8/gems/chef-11.4.0/lib\"},\"ohai\":{\"version\":\"6.16.0\",\"ohai_root\":\"/usr/lib64/ruby/gems/1.8/gems/ohai-6.16.0/lib/ohai\"}},\"counters\":{\"network\":{\"interfaces\":{\"eth0\":{\"rx\":{\"drop\":\"0\",\"bytes\":\"245927\",\"overrun\":\"0\",\"packets\":\"2172\",\"errors\":\"0\"},\"tx\":{\"queuelen\":\"1000\",\"drop\":\"0\",\"collisions\":\"0\",\"bytes\":\"43274\",\"carrier\":\"0\",\"packets\":\"146\",\"errors\":\"0\"}},\"lo\":{\"rx\":{\"drop\":\"0\",\"bytes\":\"328\",\"overrun\":\"0\",\"packets\":\"6\",\"errors\":\"0\"},\"tx\":{\"drop\":\"0\",\"collisions\":\"0\",\"bytes\":\"328\",\"carrier\":\"0\",\"packets\":\"6\",\"errors\":\"0\"}}}}},\"ipaddress\":\"172.30.5.13\"},\"name\":\"sdc15102013d.novalocal\",\"default\":{}}\n";

        chefNodeDaoRestImpl.setPropertiesProvider(propertiesProvider);
        chefNodeDaoRestImpl.setDigester(mixlibAuthenticationDigester);
        chefNodeDaoRestImpl.setClientConfig(clientConfig);
        WebTarget webResource = mock(WebTarget.class);
        Invocation.Builder builder = mock(Invocation.Builder.class);
        Response response = mock(Response.class);
        String responseJSON = "{\"json_class\":\"Chef::Node\",\"override\":{},\"chef_environment\":\"_default\",\"chef_type\":\"node\",\"normal\":{\"tags\":[]},\"run_list\":[],\"automatic\":{\"os\":\"linux\",\"cpu\":{\"real\":0,\"0\":{\"model\":\"2\",\"flags\":[\"fpu\",\"de\",\"pse\",\"tsc\",\"msr\",\"pae\",\"mce\",\"cx8\",\"apic\",\"mtrr\",\"pge\",\"mca\",\"cmov\",\"pse36\",\"clflush\",\"mmx\",\"fxsr\",\"sse\",\"sse2\",\"syscall\",\"nx\",\"lm\",\"up\",\"rep_good\",\"unfair_spinlock\",\"pni\",\"vmx\",\"cx16\",\"popcnt\",\"hypervisor\",\"lahf_lm\"],\"mhz\":\"2394.230\",\"model_name\":\"QEMU Virtual CPU version 1.0\",\"family\":\"6\",\"vendor_id\":\"GenuineIntel\",\"stepping\":\"3\",\"cache_size\":\"4096 KB\"},\"total\":1},\"current_user\":\"root\",\"ip6address\":\"fe80::f816:3eff:fe5c:ced3\",\"virtualization\":{\"role\":\"guest\",\"system\":\"kvm\"},\"languages\":{\"ruby\":{\"bin_dir\":\"/usr/bin\",\"host_os\":\"linux-gnu\",\"target_os\":\"linux\",\"host\":\"x86_64-redhat-linux-gnu\",\"host_cpu\":\"x86_64\",\"version\":\"1.8.7\",\"host_vendor\":\"redhat\",\"target\":\"x86_64-redhat-linux-gnu\",\"gem_bin\":\"/usr/bin/gem\",\"platform\":\"x86_64-linux\",\"release_date\":\"2011-06-30\",\"ruby_bin\":\"/usr/bin/ruby\",\"target_cpu\":\"x86_64\",\"gems_dir\":\"/usr/lib64/ruby/gems/1.8\",\"target_vendor\":\"redhat\"},\"perl\":{\"version\":\"5.10.1\",\"archname\":\"x86_64-linux-thread-multi\"},\"python\":{\"builddate\":\"Dec 7 2011, 20:48:22\",\"version\":\"2.6.6\"},\"lua\":{\"version\":\"5.1.4\"},\"java\":{\"runtime\":{\"name\":\"OpenJDK Runtime Environment (IcedTea6 1.10.4)\",\"build\":\"rhel-1.41.1.10.4.el6-x86_64\"},\"version\":\"1.6.0_22\",\"hotspot\":{\"name\":\"OpenJDK 64-Bit Server VM\",\"build\":\"20.0-b11, mixed mode\"}}},\"lsb\":{\"release\":\"6.2\",\"id\":\"CentOS\",\"description\":\"CentOS release 6.2 (Final)\",\"codename\":\"Final\"},\"domain\":\"novalocal\",\"platform_version\":\"6.2\",\"platform_family\":\"rhel\",\"roles\":[],\"memory\":{\"writeback\":\"0kB\",\"inactive\":\"103500kB\",\"vmalloc_chunk\":\"34359731080kB\",\"dirty\":\"116kB\",\"vmalloc_used\":\"3528kB\",\"page_tables\":\"3984kB\",\"committed_as\":\"193392kB\",\"free\":\"286480kB\",\"slab\":\"27276kB\",\"nfs_unstable\":\"0kB\",\"anon_pages\":\"112640kB\",\"active\":\"70288kB\",\"swap\":{\"free\":\"0kB\",\"cached\":\"0kB\",\"total\":\"0kB\"},\"buffers\":\"11468kB\",\"bounce\":\"0kB\",\"commit_limit\":\"251360kB\",\"vmalloc_total\":\"34359738367kB\",\"mapped\":\"12436kB\",\"slab_reclaimable\":\"7728kB\",\"cached\":\"49652kB\",\"total\":\"502720kB\",\"slab_unreclaim\":\"19548kB\"},\"idletime\":\"10 minutes 11 seconds\",\"command\":{\"ps\":\"ps -ef\"},\"idletime_seconds\":611,\"ohai_time\":2381841405.78531,\"uptime\":\"10 minutes 55 seconds\",\"dmi\":{\"chassis\":{\"lock\":\"Not Present\",\"type\":\"Other\",\"asset_tag\":\"Not Specified\",\"version\":\"Not Specified\",\"power_supply_state\":\"Safe\",\"security_status\":\"Unknown\",\"boot_up_state\":\"Safe\",\"height\":\"Unspecified\",\"all_records\":[{\"Lock\":\"Not Present\",\"size\":\"3\",\"Serial Number\":\"Not Specified\",\"Version\":\"Not Specified\",\"Boot-up State\":\"Safe\",\"Asset Tag\":\"Not Specified\",\"Type\":\"Other\",\"Security Status\":\"Unknown\",\"Number Of Power Cords\":\"Unspecified\",\"application_identifier\":\"Chassis Information\",\"Power Supply State\":\"Safe\",\"record_id\":\"0x0300\",\"OEM Information\":\"0x00000000\",\"Height\":\"Unspecified\",\"Thermal State\":\"Safe\",\"Manufacturer\":\"Bochs\"}],\"serial_number\":\"Not Specified\",\"manufacturer\":\"Bochs\",\"number_of_power_cords\":\"Unspecified\",\"oem_information\":\"0x00000000\",\"thermal_state\":\"Safe\"},\"structures\":{\"size\":\"263\",\"count\":\"10\"},\"dmidecode_version\":\"2.11\",\"processor\":{\"locator\":\"DIMM 0\",\"status\":\"No errors detected\",\"bank_locator\":\"Not Specified\",\"voltage\":\"Unknown\",\"max_speed\":\"2000 MHz\",\"size\":\"512 MB\",\"l2_cache_handle\":\"Not Provided\",\"form_factor\":\"DIMM\",\"data_width\":\"64 bits\",\"type\":\"RAM\",\"range_size\":\"512 MB\",\"total_width\":\"64 bits\",\"physical_device_handle\":\"0x1100\",\"number_of_devices\":\"1\",\"id\":\"23 06 00 00 FD FB 8B 07\",\"maximum_capacity\":\"512 MB\",\"partition_width\":\"1\",\"type_detail\":\"None\",\"l1_cache_handle\":\"Not Provided\",\"partition_row_position\":\"1\",\"physical_array_handle\":\"0x1000\",\"version\":\"Not Specified\",\"use\":\"System Memory\",\"error_correction_type\":\"Multi-bit ECC\",\"starting_address\":\"0x00000000000\",\"all_records\":[{\"Array Handle\":\"0x1000\",\"L1 Cache Handle\":\"Not Provided\",\"Voltage\":\"Unknown\",\"Locator\":\"DIMM 0\",\"ID\":\"23 06 00 00 FD FB 8B 07\",\"size\":\"4\",\"Set\":\"None\",\"Size\":\"512 MB\",\"Number Of Devices\":\"1\",\"Version\":\"Not Specified\",\"Status\":\"No errors detected\",\"Maximum Capacity\":\"512 MB\",\"Partition Width\":\"1\",\"Partition Row Position\":\"1\",\"Type Detail\":\"None\",\"External Clock\":\"Unknown\",\"Memory Array Mapped Address Handle\":\"0x1300\",\"Bank Locator\":\"Not Specified\",\"Total Width\":\"64 bits\",\"Current Speed\":\"2000 MHz\",\"Type\":\"RAM\",\"Data Width\":\"64 bits\",\"L3 Cache Handle\":\"Not Provided\",\"Socket Designation\":\"CPU 1\",\"Max Speed\":\"2000 MHz\",\"Range Size\":\"512 MB\",\"Form Factor\":\"DIMM\",\"Physical Array Handle\":\"0x1000\",\"Use\":\"System Memory\",\"application_identifier\":\"End Of Table\",\"Starting Address\":\"0x00000000000\",\"Ending Address\":\"0x0001FFFFFFF\",\"Physical Device Handle\":\"0x1100\",\"Upgrade\":\"Other\",\"record_id\":\"0x0401\",\"Error Information Handle\":\"0x0307\",\"Manufacturer\":\"Bochs\",\"L2 Cache Handle\":\"Not Provided\",\"Error Correction Type\":\"Multi-bit ECC\",\"Family\":\"Other\",\"Location\":\"Other\"}],\"current_speed\":\"2000 MHz\",\"location\":\"Other\",\"socket_designation\":\"CPU 1\",\"manufacturer\":\"Bochs\",\"family\":\"Other\",\"set\":\"None\",\"memory_array_mapped_address_handle\":\"0x1300\",\"external_clock\":\"Unknown\",\"array_handle\":\"0x1000\",\"upgrade\":\"Other\",\"l3_cache_handle\":\"Not Provided\",\"ending_address\":\"0x0001FFFFFFF\",\"error_information_handle\":\"0x0307\"},\"smbios_version\":\"2.4\",\"bios\":{\"version\":\"Bochs\",\"bios_revision\":\"1.0\",\"all_records\":[{\"size\":\"0\",\"Characteristics\":{\"BIOS characteristics not supported\":null,\"Targeted content distribution is supported\":null},\"Release Date\":\"01/01/2007\",\"Runtime Size\":\"96 kB\",\"Version\":\"Bochs\",\"BIOS Revision\":\"1.0\",\"application_identifier\":\"BIOS Information\",\"record_id\":\"0x0000\",\"ROM Size\":\"64 kB\",\"Vendor\":\"Bochs\",\"Address\":\"0xE8000\"}],\"runtime_size\":\"96 kB\",\"release_date\":\"01/01/2007\",\"address\":\"0xE8000\",\"rom_size\":\"64 kB\",\"vendor\":\"Bochs\"},\"system\":{\"wake_up_type\":\"Power Switch\",\"version\":\"Not Specified\",\"product_name\":\"Bochs\",\"all_records\":[{\"Wake-up Type\":\"Power Switch\",\"Product Name\":\"Bochs\",\"size\":\"1\",\"Serial Number\":\"Not Specified\",\"Version\":\"Not Specified\",\"UUID\":\"A378FD62-BBC8-4BA4-8C8D-8D196789708C\",\"SKU Number\":\"Not Specified\",\"application_identifier\":\"System Information\",\"record_id\":\"0x0100\",\"Manufacturer\":\"Bochs\",\"Family\":\"Not Specified\"}],\"serial_number\":\"Not Specified\",\"manufacturer\":\"Bochs\",\"family\":\"Not Specified\",\"sku_number\":\"Not Specified\",\"uuid\":\"A378FD62-BBC8-4BA4-8C8D-8D196789708C\"}},\"block_device\":{\"ram0\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram1\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram2\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram3\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram4\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram5\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram6\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram7\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram8\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram9\":{\"size\":\"32768\",\"removable\":\"0\"},\"vda\":{\"size\":\"10485760\",\"vendor\":\"6900\",\"removable\":\"0\"},\"loop0\":{\"size\":\"0\",\"removable\":\"0\"},\"loop1\":{\"size\":\"0\",\"removable\":\"0\"},\"loop2\":{\"size\":\"0\",\"removable\":\"0\"},\"loop3\":{\"size\":\"0\",\"removable\":\"0\"},\"loop4\":{\"size\":\"0\",\"removable\":\"0\"},\"loop5\":{\"size\":\"0\",\"removable\":\"0\"},\"loop6\":{\"size\":\"0\",\"removable\":\"0\"},\"loop7\":{\"size\":\"0\",\"removable\":\"0\"},\"ram10\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram11\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram12\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram13\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram14\":{\"size\":\"32768\",\"removable\":\"0\"},\"ram15\":{\"size\":\"32768\",\"removable\":\"0\"}},\"platform\":\"centos\",\"fqdn\":\"sdc15102013d.novalocal\",\"filesystem\":{\"/sys\":{\"mount\":\"/sys\",\"fs_type\":\"sysfs\",\"mount_options\":[\"rw\",\"relatime\"]},\"/dev/vda1\":{\"mount\":\"/\",\"fs_type\":\"ext4\",\"percent_used\":\"44%\",\"kb_size\":\"5159552\",\"mount_options\":[\"rw\"],\"uuid\":\"93ab5295-5527-4919-8098-ce9b49bc7a65\",\"kb_used\":\"2142816\",\"kb_available\":\"2754648\"},\"/proc/bus/usb\":{\"mount\":\"/proc/bus/usb\",\"fs_type\":\"usbfs\",\"mount_options\":[\"rw\",\"relatime\"]},\"tmpfs\":{\"mount\":\"/dev/shm\",\"fs_type\":\"tmpfs\",\"percent_used\":\"0%\",\"kb_size\":\"251360\",\"mount_options\":[\"rw\"],\"kb_used\":\"0\",\"kb_available\":\"251360\"},\"rootfs\":{\"mount\":\"/\",\"fs_type\":\"rootfs\",\"mount_options\":[\"rw\"]},\"sysfs\":{\"mount\":\"/sys\",\"fs_type\":\"sysfs\",\"mount_options\":[\"rw\"]},\"/proc\":{\"mount\":\"/proc\",\"fs_type\":\"proc\",\"mount_options\":[\"rw\",\"relatime\"]},\"devpts\":{\"mount\":\"/dev/pts\",\"fs_type\":\"devpts\",\"mount_options\":[\"rw\",\"gid=5\",\"mode=620\"]},\"/etc/auto.misc\":{\"mount\":\"/misc\",\"fs_type\":\"autofs\",\"mount_options\":[\"rw\",\"relatime\",\"fd=7\",\"pgrp=1402\",\"timeout=300\",\"minproto=5\",\"maxproto=5\",\"indirect\"]},\"none\":{\"mount\":\"/proc/sys/fs/binfmt_misc\",\"fs_type\":\"binfmt_misc\",\"mount_options\":[\"rw\"]},\"udev\":{\"mount\":\"/dev\",\"fs_type\":\"devtmpfs\",\"mount_options\":[\"rw\",\"relatime\",\"size=241740k\",\"nr_inodes=60435\",\"mode=755\"]},\"proc\":{\"mount\":\"/proc\",\"fs_type\":\"proc\",\"mount_options\":[\"rw\"]},\"sunrpc\":{\"mount\":\"/var/lib/nfs/rpc_pipefs\",\"fs_type\":\"rpc_pipefs\",\"mount_options\":[\"rw\"]},\"-hosts\":{\"mount\":\"/net\",\"fs_type\":\"autofs\",\"mount_options\":[\"rw\",\"relatime\",\"fd=13\",\"pgrp=1402\",\"timeout=300\",\"minproto=5\",\"maxproto=5\",\"indirect\"]}},\"kernel\":{\"release\":\"2.6.32-220.el6.x86_64\",\"os\":\"GNU/Linux\",\"machine\":\"x86_64\",\"version\":\"#1 SMP Tue Dec 6 19:48:22 GMT 2011\",\"modules\":{\"virtio_balloon\":{\"size\":\"4347\",\"refcount\":\"0\"},\"microcode\":{\"size\":\"112594\",\"refcount\":\"0\"},\"i2c_core\":{\"size\":\"31276\",\"refcount\":\"1\"},\"mbcache\":{\"size\":\"8144\",\"refcount\":\"1\"},\"ip6table_filter\":{\"size\":\"2889\",\"refcount\":\"1\"},\"i2c_piix4\":{\"size\":\"12608\",\"refcount\":\"0\"},\"ip6t_REJECT\":{\"size\":\"4628\",\"refcount\":\"2\"},\"ata_generic\":{\"size\":\"3837\",\"refcount\":\"0\"},\"virtio_pci\":{\"size\":\"6687\",\"refcount\":\"0\"},\"dm_log\":{\"size\":\"10122\",\"refcount\":\"2\"},\"dm_mod\":{\"size\":\"81500\",\"refcount\":\"2\"},\"nf_conntrack\":{\"size\":\"79453\",\"refcount\":\"2\"},\"ipv6\":{\"size\":\"322029\",\"refcount\":\"29\"},\"virtio_net\":{\"size\":\"15839\",\"refcount\":\"0\"},\"ata_piix\":{\"size\":\"22846\",\"refcount\":\"0\"},\"ip6_tables\":{\"size\":\"19458\",\"refcount\":\"1\"},\"xt_state\":{\"size\":\"1492\",\"refcount\":\"2\"},\"virtio_blk\":{\"size\":\"6671\",\"refcount\":\"2\"},\"nf_conntrack_ipv6\":{\"size\":\"8748\",\"refcount\":\"2\"},\"dm_mirror\":{\"size\":\"14101\",\"refcount\":\"0\"},\"autofs4\":{\"size\":\"26888\",\"refcount\":\"3\"},\"pata_acpi\":{\"size\":\"3701\",\"refcount\":\"0\"},\"dm_region_hash\":{\"size\":\"12170\",\"refcount\":\"1\"},\"nf_defrag_ipv6\":{\"size\":\"12182\",\"refcount\":\"1\"},\"ext4\":{\"size\":\"364410\",\"refcount\":\"1\"},\"virtio_ring\":{\"size\":\"7729\",\"refcount\":\"4\"},\"jbd2\":{\"size\":\"88738\",\"refcount\":\"1\"},\"sunrpc\":{\"size\":\"243758\",\"refcount\":\"1\"},\"virtio\":{\"size\":\"4890\",\"refcount\":\"4\"}},\"name\":\"Linux\"},\"hostname\":\"sdc15102013d\",\"network\":{\"default_interface\":\"eth0\",\"interfaces\":{\"eth0\":{\"type\":\"eth\",\"addresses\":{\"172.30.5.13\":{\"scope\":\"Global\",\"netmask\":\"255.255.255.0\",\"family\":\"inet\",\"broadcast\":\"172.30.5.255\",\"prefixlen\":\"24\"},\"fe80::f816:3eff:fe5c:ced3\":{\"scope\":\"Link\",\"family\":\"inet6\",\"prefixlen\":\"64\"},\"FA:16:3E:5C:CE:D3\":{\"family\":\"lladdr\"}},\"arp\":{\"172.30.5.1\":\"54:52:00:37:68:64\"},\"flags\":[\"BROADCAST\",\"MULTICAST\",\"UP\",\"LOWER_UP\"],\"number\":\"0\",\"routes\":[{\"scope\":\"link\",\"destination\":\"172.30.5.0/24\",\"proto\":\"kernel\",\"src\":\"172.30.5.13\",\"family\":\"inet\"},{\"scope\":\"link\",\"destination\":\"169.254.0.0/16\",\"family\":\"inet\",\"metric\":\"1002\"},{\"via\":\"172.30.5.1\",\"destination\":\"default\",\"family\":\"inet\"},{\"destination\":\"fe80::/64\",\"proto\":\"kernel\",\"family\":\"inet6\",\"metric\":\"256\"}],\"encapsulation\":\"Ethernet\",\"mtu\":\"1500\",\"state\":\"up\"},\"lo\":{\"addresses\":{\"::1\":{\"scope\":\"Node\",\"family\":\"inet6\",\"prefixlen\":\"128\"},\"127.0.0.1\":{\"scope\":\"Node\",\"netmask\":\"255.0.0.0\",\"family\":\"inet\",\"prefixlen\":\"8\"}},\"flags\":[\"LOOPBACK\",\"UP\",\"LOWER_UP\"],\"routes\":[{\"destination\":\"unreachable\",\"family\":\"inet6\",\"metric\":\"1024\"}],\"encapsulation\":\"Loopback\",\"mtu\":\"16436\",\"state\":\"unknown\"}},\"default_gateway\":\"172.30.5.1\"},\"keys\":{\"ssh\":{}},\"etc\":{\"passwd\":{\"bin\":{\"dir\":\"/bin\",\"gid\":1,\"uid\":1,\"gecos\":\"bin\",\"shell\":\"/sbin/nologin\"},\"uucp\":{\"dir\":\"/var/spool/uucp\",\"gid\":14,\"uid\":10,\"gecos\":\"uucp\",\"shell\":\"/sbin/nologin\"},\"haldaemon\":{\"dir\":\"/\",\"gid\":68,\"uid\":68,\"gecos\":\"HAL daemon\",\"shell\":\"/sbin/nologin\"},\"localadmin\":{\"dir\":\"/home/localadmin\",\"gid\":500,\"uid\":500,\"gecos\":\"\",\"shell\":\"/bin/bash\"},\"gopher\":{\"dir\":\"/var/gopher\",\"gid\":30,\"uid\":13,\"gecos\":\"gopher\",\"shell\":\"/sbin/nologin\"},\"vcsa\":{\"dir\":\"/dev\",\"gid\":69,\"uid\":69,\"gecos\":\"virtual console memory owner\",\"shell\":\"/sbin/nologin\"},\"abrt\":{\"dir\":\"/etc/abrt\",\"gid\":173,\"uid\":173,\"gecos\":\"\",\"shell\":\"/sbin/nologin\"},\"nfsnobody\":{\"dir\":\"/var/lib/nfs\",\"gid\":65534,\"uid\":65534,\"gecos\":\"Anonymous NFS User\",\"shell\":\"/sbin/nologin\"},\"daemon\":{\"dir\":\"/sbin\",\"gid\":2,\"uid\":2,\"gecos\":\"daemon\",\"shell\":\"/sbin/nologin\"},\"saslauth\":{\"dir\":\"/var/empty/saslauth\",\"gid\":76,\"uid\":499,\"gecos\":\"\\\"Saslauthd user\\\"\",\"shell\":\"/sbin/nologin\"},\"lp\":{\"dir\":\"/var/spool/lpd\",\"gid\":7,\"uid\":4,\"gecos\":\"lp\",\"shell\":\"/sbin/nologin\"},\"tcpdump\":{\"dir\":\"/\",\"gid\":72,\"uid\":72,\"gecos\":\"\",\"shell\":\"/sbin/nologin\"},\"games\":{\"dir\":\"/usr/games\",\"gid\":100,\"uid\":12,\"gecos\":\"games\",\"shell\":\"/sbin/nologin\"},\"halt\":{\"dir\":\"/sbin\",\"gid\":0,\"uid\":7,\"gecos\":\"halt\",\"shell\":\"/sbin/halt\"},\"shutdown\":{\"dir\":\"/sbin\",\"gid\":0,\"uid\":6,\"gecos\":\"shutdown\",\"shell\":\"/sbin/shutdown\"},\"oprofile\":{\"dir\":\"/home/oprofile\",\"gid\":16,\"uid\":16,\"gecos\":\"Special user account to be used by OProfile\",\"shell\":\"/sbin/nologin\"},\"rpc\":{\"dir\":\"/var/cache/rpcbind\",\"gid\":32,\"uid\":32,\"gecos\":\"Rpcbind Daemon\",\"shell\":\"/sbin/nologin\"},\"nobody\":{\"dir\":\"/\",\"gid\":99,\"uid\":99,\"gecos\":\"Nobody\",\"shell\":\"/sbin/nologin\"},\"sync\":{\"dir\":\"/sbin\",\"gid\":0,\"uid\":5,\"gecos\":\"sync\",\"shell\":\"/bin/sync\"},\"sshd\":{\"dir\":\"/var/empty/sshd\",\"gid\":74,\"uid\":74,\"gecos\":\"Privilege-separated SSH\",\"shell\":\"/sbin/nologin\"},\"mail\":{\"dir\":\"/var/spool/mail\",\"gid\":12,\"uid\":8,\"gecos\":\"mail\",\"shell\":\"/sbin/nologin\"},\"dbus\":{\"dir\":\"/\",\"gid\":81,\"uid\":81,\"gecos\":\"System message bus\",\"shell\":\"/sbin/nologin\"},\"root\":{\"dir\":\"/root\",\"gid\":0,\"uid\":0,\"gecos\":\"root\",\"shell\":\"/bin/bash\"},\"qpidd\":{\"dir\":\"/var/lib/qpidd\",\"gid\":499,\"uid\":498,\"gecos\":\"Owner of Qpidd Daemons\",\"shell\":\"/sbin/nologin\"},\"ftp\":{\"dir\":\"/var/ftp\",\"gid\":50,\"uid\":14,\"gecos\":\"FTP User\",\"shell\":\"/sbin/nologin\"},\"adm\":{\"dir\":\"/var/adm\",\"gid\":4,\"uid\":3,\"gecos\":\"adm\",\"shell\":\"/sbin/nologin\"},\"avahi\":{\"dir\":\"/var/run/avahi-daemon\",\"gid\":70,\"uid\":70,\"gecos\":\"Avahi mDNS/DNS-SD Stack\",\"shell\":\"/sbin/nologin\"},\"rpcuser\":{\"dir\":\"/var/lib/nfs\",\"gid\":29,\"uid\":29,\"gecos\":\"RPC Service User\",\"shell\":\"/sbin/nologin\"},\"operator\":{\"dir\":\"/root\",\"gid\":0,\"uid\":11,\"gecos\":\"operator\",\"shell\":\"/sbin/nologin\"},\"ntp\":{\"dir\":\"/etc/ntp\",\"gid\":38,\"uid\":38,\"gecos\":\"\",\"shell\":\"/sbin/nologin\"},\"gdm\":{\"dir\":\"/var/lib/gdm\",\"gid\":42,\"uid\":42,\"gecos\":\"\",\"shell\":\"/sbin/nologin\"},\"postfix\":{\"dir\":\"/var/spool/postfix\",\"gid\":89,\"uid\":89,\"gecos\":\"\",\"shell\":\"/sbin/nologin\"}},\"group\":{\"bin\":{\"gid\":1,\"members\":[\"root\",\"bin\",\"daemon\"]},\"tty\":{\"gid\":5,\"members\":[]},\"dip\":{\"gid\":40,\"members\":[]},\"postdrop\":{\"gid\":90,\"members\":[]},\"uucp\":{\"gid\":14,\"members\":[\"uucp\"]},\"lock\":{\"gid\":54,\"members\":[]},\"haldaemon\":{\"gid\":68,\"members\":[\"haldaemon\"]},\"man\":{\"gid\":15,\"members\":[]},\"cgred\":{\"gid\":496,\"members\":[]},\"localadmin\":{\"gid\":500,\"members\":[]},\"gopher\":{\"gid\":30,\"members\":[]},\"vcsa\":{\"gid\":69,\"members\":[]},\"abrt\":{\"gid\":173,\"members\":[]},\"nfsnobody\":{\"gid\":65534,\"members\":[]},\"daemon\":{\"gid\":2,\"members\":[\"root\",\"bin\",\"daemon\"]},\"saslauth\":{\"gid\":76,\"members\":[]},\"lp\":{\"gid\":7,\"members\":[\"daemon\",\"lp\"]},\"sys\":{\"gid\":3,\"members\":[\"root\",\"bin\",\"adm\"]},\"tcpdump\":{\"gid\":72,\"members\":[]},\"games\":{\"gid\":20,\"members\":[]},\"audio\":{\"gid\":63,\"members\":[]},\"utmp\":{\"gid\":22,\"members\":[]},\"floppy\":{\"gid\":19,\"members\":[]},\"slocate\":{\"gid\":21,\"members\":[]},\"video\":{\"gid\":39,\"members\":[]},\"oprofile\":{\"gid\":16,\"members\":[]},\"desktop_admin_r\":{\"gid\":495,\"members\":[]},\"rpc\":{\"gid\":32,\"members\":[]},\"tape\":{\"gid\":33,\"members\":[]},\"nobody\":{\"gid\":99,\"members\":[]},\"utempter\":{\"gid\":35,\"members\":[]},\"sshd\":{\"gid\":74,\"members\":[]},\"desktop_user_r\":{\"gid\":494,\"members\":[]},\"mail\":{\"gid\":12,\"members\":[\"mail\",\"postfix\"]},\"stapdev\":{\"gid\":498,\"members\":[]},\"disk\":{\"gid\":6,\"members\":[\"root\"]},\"mem\":{\"gid\":8,\"members\":[]},\"dbus\":{\"gid\":81,\"members\":[]},\"stapusr\":{\"gid\":497,\"members\":[]},\"root\":{\"gid\":0,\"members\":[\"root\"]},\"qpidd\":{\"gid\":499,\"members\":[]},\"dialout\":{\"gid\":18,\"members\":[]},\"ftp\":{\"gid\":50,\"members\":[]},\"adm\":{\"gid\":4,\"members\":[\"root\",\"adm\",\"daemon\"]},\"avahi\":{\"gid\":70,\"members\":[]},\"cdrom\":{\"gid\":11,\"members\":[]},\"rpcuser\":{\"gid\":29,\"members\":[]},\"ntp\":{\"gid\":38,\"members\":[]},\"gdm\":{\"gid\":42,\"members\":[]},\"wheel\":{\"gid\":10,\"members\":[\"root\"]},\"kmem\":{\"gid\":9,\"members\":[]},\"users\":{\"gid\":100,\"members\":[]},\"postfix\":{\"gid\":89,\"members\":[]}}},\"macaddress\":\"FA:16:3E:5C:CE:D3\",\"recipes\":[],\"os_version\":\"2.6.32-220.el6.x86_64\",\"uptime_seconds\":655,\"chef_packages\":{\"chef\":{\"version\":\"11.4.0\",\"chef_root\":\"/usr/lib64/ruby/gems/1.8/gems/chef-11.4.0/lib\"},\"ohai\":{\"version\":\"6.16.0\",\"ohai_root\":\"/usr/lib64/ruby/gems/1.8/gems/ohai-6.16.0/lib/ohai\"}},\"counters\":{\"network\":{\"interfaces\":{\"eth0\":{\"rx\":{\"drop\":\"0\",\"bytes\":\"245927\",\"overrun\":\"0\",\"packets\":\"2172\",\"errors\":\"0\"},\"tx\":{\"queuelen\":\"1000\",\"drop\":\"0\",\"collisions\":\"0\",\"bytes\":\"43274\",\"carrier\":\"0\",\"packets\":\"146\",\"errors\":\"0\"}},\"lo\":{\"rx\":{\"drop\":\"0\",\"bytes\":\"328\",\"overrun\":\"0\",\"packets\":\"6\",\"errors\":\"0\"},\"tx\":{\"drop\":\"0\",\"collisions\":\"0\",\"bytes\":\"328\",\"carrier\":\"0\",\"packets\":\"6\",\"errors\":\"0\"}}}}},\"ipaddress\":\"172.30.5.13\"},\"name\":\"sdc15102013d.novalocal\",\"default\":{}}\n";

        // when
        when(chefNode.toJson()).thenReturn(payload);
        when(chefNode.getName()).thenReturn("nodename");
        when(propertiesProvider.getProperty(anyString())).thenReturn("http://localhost");
        when(clientConfig.getClient()).thenReturn(client);
        when(client.target(anyString())).thenReturn(webResource);
        when(webResource.path("/nodes/nodename")).thenReturn(webResource);

        when(webResource.request(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.accept(MediaType.APPLICATION_JSON)).thenReturn(builder);
        when(builder.header(anyString(), anyObject())).thenReturn(builder);
        when(builder.put(any(Entity.class))).thenReturn(response);
        when(response.readEntity(String.class)).thenReturn(responseJSON);
        chefNodeDaoRestImpl.setOpenStackRegion(openStackRegion);
        when(openStackRegion.getChefServerEndPoint()).thenReturn("http://chefserver");

        // then
        chefNodeDaoRestImpl.updateNode(chefNode, "token");

        verify(client, times(1)).target(anyString());
        verify(propertiesProvider, atLeastOnce()).getProperty(anyString());
        verify(chefNode).toJson();
    }
}
