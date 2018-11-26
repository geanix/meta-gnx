DESCRIPTION = "Configuration snippets for systemd"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit systemd allarch

SRC_URI = " \
    file://90-wired.network \
"

do_configure[noexex] = "1"
do_compile[noexec] = "1"

do_install_append() {
    # networkd config
    install -d ${D}${systemd_unitdir}/network
    install -m0644 ${WORKDIR}/90-wired.network ${D}${systemd_unitdir}/network
}

FILES_${PN} = "${systemd_unitdir}"
