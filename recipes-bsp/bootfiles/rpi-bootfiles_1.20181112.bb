DESCRIPTION = "Closed source binary files to help boot the ARM on the BCM2835."
LICENSE = "Proprietary"
LIC_FILES_CHKSUM = "file://boot/LICENCE.broadcom;md5=4a4d169737c0786fb9482bb6d30401d1"

inherit deploy

SRC_URI = "https://github.com/raspberrypi/firmware/archive/${PV}.tar.gz"
SRC_URI[md5sum] = "1d041e337eb0de954989d9f6b4a135ec"
SRC_URI[sha256sum] = "eccb3221fe5fbf9e44e7d1a79b2f401eb7527a41fb8ea7fe7231bc188a8ff50b"
S = "${WORKDIR}/firmware-${PV}"

# This package dosn't require anything to function
INHIBIT_DEFAULT_DEPS = "1"

# Make sure it is only built for raspberry pi's
COMPATIBLE_MACHINE = "^rpi(2|3)?$"

# Override default architechture (which is arm) and skip the check
PACKAGE_ARCH = "${MACHINE_ARCH}"
INSANE_SKIP_${PN} += "arch"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}/boot

    for i in fixup.dat start.elf bootcode.bin; do
        install -m 644 boot/$i ${D}/boot
    done
}

RDEPENDS_${PN} += "rpi-config"
FILES_${PN} = "/boot/"

do_deploy() {
    install -d ${DEPLOYDIR}/${PN}
    install -m644 ${D}/boot/* ${DEPLOYDIR}/${PN}/
}

addtask deploy before do_build after do_install
do_deploy[dirs] += "${DEPLOYDIR}/${PN}"
