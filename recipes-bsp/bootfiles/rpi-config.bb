SUMMARY = "Commented config.txt file for the Raspberry Pi"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit deploy allarch

# This package dosn't require anything to function
INHIBIT_DEFAULT_DEPS = "1"

# Make sure it is only built for raspberry pi's
COMPATIBLE_MACHINE = "^rpi(2|3)?$"

do_configure[noexec] = "1"
do_compile[noexec] = "1"

do_install() {
    install -d ${D}/boot
    echo "enable_uart=1" > ${D}/boot/config.txt
    echo "arm_control=0x200" >> ${D}/boot/config.txt
    echo "kernel=u-boot.bin" >> ${D}/boot/config.txt
}

FILES_${PN} = "/boot/config.txt"

do_deploy() {
    install -d ${DEPLOYDIR}/${PN}
    install -m644 ${D}/boot/* ${DEPLOYDIR}/${PN}/
}

addtask deploy before do_build after do_install
do_deploy[dirs] += "${DEPLOYDIR}/${PN}"
