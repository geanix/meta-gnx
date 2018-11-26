SUMMARY = "Mainline kernel package"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=bbea815ee2795b2f4230826c0c6b8814"

inherit kernel

DEPENDS += "lzop"

SRC_URI = "git://github.com/geanix/linux.git;branch=gnx-4.19.y"
SRCREV = "98a661ea85a8b46aa3e4bff1d900e289011e2d2a"
S = "${WORKDIR}/git"

# The kernel class expects a defconfig in ${WORKDIR}, so tell it which one to use
DEFAULT_KERNEL_DEFCONFIG_arm     = "multi_v7_defconfig"
DEFAULT_KERNEL_DEFCONFIG_aarch64 = "defconfig"
DEFAULT_KERNEL_DEFCONFIG_x86     = "i386_defconfig"
DEFAULT_KERNEL_DEFCONFIG_x86_64  = "x86_64_defconfig"
KERNEL_DEFCONFIG ?= "${DEFAULT_KERNEL_DEFCONFIG}"

# enable support for device tree overlays
EXTRA_OEMAKE += "DTC_FLAGS=-@"

# Pick up configured default configurations from the linux source tree
do_configure_prepend() {
    if [ -n "${KERNEL_DEFCONFIG}" ]; then
        cp ${S}/arch/${ARCH}/configs/${KERNEL_DEFCONFIG} ${B}/.config
    fi
}

# install devicetree blobs where those are configured
RRECOMMENDS_kernel-image += "kernel-devicetree"
