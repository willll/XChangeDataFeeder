>[TODO]


* Migrate to XChange 5.0 that include the stream APIs

>[Exchanges]

* Bitstamp : Not working, API key missing ?

* hitBC :

WARNING: An illegal reflective access operation has occurred
WARNING: Illegal reflective access by org.knowm.xchange.hitbtc.v2.HitbtcExchange (file:/home/will/.m2/repository/org/knowm/xchange/xchange-hitbtc/4.4.2/xchange-hitbtc-4.4.2.jar) to field java.net.HttpURLConnection.methods
WARNING: Please consider reporting this to the maintainers of org.knowm.xchange.hitbtc.v2.HitbtcExchange
WARNING: Use --illegal-access=warn to enable warnings of further illegal reflective access operations
WARNING: All illegal access operations will be denied in a future release

But working OK

>[Maven]

* mvn clean compile

* mvn exec:java -Dexec.mainClass=Main

* mvn exec:java -Dexec.mainClass=Main -Dexec.args="-list"

>[Eclipse]

* How to configure bitbucket in eclipse : http://crunchify.com/how-to-configure-bitbucket-git-repository-in-you-eclipse/

>[How to generate code/configuration files]

* TODO : https://github.com/willll/XChangeTemplateGenerator	

>[How to configure raspbian]

* Setup Static IP : /etc/dhcpcd.conf
(https://thepihut.com/blogs/raspberry-pi-tutorials/how-to-give-your-raspberry-pi-a-static-ip-address-update)
 
interface eth0
static ip_address=192.168.1.200/24
#static ip6_address=fd51:42f8:caae:d92e::ff/64
static routers=192.168.1.1
static domain_name_servers=192.168.1.1 8.8.8.8
 
* Install samba :

https://magpi.raspberrypi.org/articles/samba-file-server
 
* Install Java :
(https://linuxize.com/post/install-java-on-raspberry-pi/)

sudo apt update
sudo apt install openjdk-8-jdk
sudo apt install maven

* Setup hard drive :

https://www.raspberrypi.org/documentation/configuration/external-storage.md

* InfluxDB

https://pimylifeup.com/raspberry-pi-influxdb/

https://stackoverflow.com/questions/44454836/influxdb-storage-size-on-disk

+ enable HTTP

* Chronograf 

sudo apt install chronograf
sudo systemctl unmask chronograf
sudo systemctl enable chronograf
sudo systemctl start chronograf





