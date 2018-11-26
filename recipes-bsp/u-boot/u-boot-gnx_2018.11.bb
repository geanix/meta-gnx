HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://Licenses/README;md5=30503fd321432fc713238f582193b78e"

SRC_URI = "git://github.com/geanix/u-boot.git;branch=gnx-2018.11"
SRCREV = "9e4d5034e6526a706820eefef87cd35123df968c"
S = "${WORKDIR}/git"

UBOOT_ENV ?= "uboot"
UBOOT_ENV_SUFFIX = "env"
UBOOT_ENV_TEXT ?= "uEnv.txt"
UBOOT_ENV_SIZE ?= "0x2000"
UBOOT_ENV_DEVICE ?= "/dev/mmcblk0"
UBOOT_ENV_OFFSET ?= "0x0000"
UBOOT_ENV_SECTOR_SIZE ?= ""
UBOOT_ENV_SECTOR_COUNT ?= ""
UBOOT_ENV_CFG ?= "${UBOOT_ENV_DEVICE}\
	${UBOOT_ENV_OFFSET}\
	${UBOOT_ENV_SIZE}\
	${UBOOT_ENV_SECTOR_SIZE}\
	${UBOOT_ENV_SECTOR_COUNT}\
"

require recipes-bsp/u-boot/u-boot.inc

DEPENDS += "bison-native bc-native dtc-native vim-native mtd-utils"

do_compile_append() {
    # generate default environment image
    ${B}/source/scripts/get_default_envs.sh ${B} > ${B}/${UBOOT_ENV_TEXT}
    ${B}/tools/mkenvimage ${UBOOT_ENV_TEXT} -s ${UBOOT_ENV_SIZE} -o ${WORKDIR}/${UBOOT_ENV_BINARY}

    # build fw_printenv (and fw_setenv)
    oe_runmake -C ${S} O=${B} CC="${CC} ${CFLAGS} ${LDFLAGS} -fPIC" envtools
}

do_install_append() {
    # install fw_printenv and friend
    install -d ${D}${base_sbindir}
    install -d ${D}${sysconfdir}
    install -m 755 ${B}/tools/env/fw_printenv ${D}${base_sbindir}/fw_printenv
    ln -s fw_printenv ${D}${base_sbindir}/fw_setenv

    # generate config for fw_{print,set}env
    cfgfile=${D}${sysconfdir}/fw_env.config
    echo "# Device    Device Offset    Env Size    Sector Size    Sector Count" > $cfgfile
    echo "${UBOOT_ENV_CFG}" >> $cfgfile

    # install fw_env "library" to let c programs access the environment
    install -d ${D}${libdir} ${D}${includedir}
    install -m 644 ${B}/tools/env/lib.a ${D}${libdir}/libubootenv.a
    install -m 644 ${S}/tools/env/fw_env.h ${D}${includedir}/fw_env.h
}

PACKAGES =+ "${PN}-fw-utils"
INSANE_SKIP_${PN} = "already-stripped"
FILES_${PN}-fw-utils = "${sysconfdir} ${base_sbindir}"
FILES_${PN} += "/boot/${BOOT_ENV_BIN}"
RRECOMMENDS_${PN} += "${PN}-fw-utils"

PACKAGES =+ "${PN}-fwenv-staticdev"
FILES_${PN}-fwenv-staticdev = "${libdir}/libubootenv.a ${includedir}/fw_env.h"
