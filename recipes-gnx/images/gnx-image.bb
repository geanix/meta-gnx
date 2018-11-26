SUMMARY = "The default rootfs image"
LICENSE = "MIT"

inherit image extrausers

IMAGE_FSTYPES = "tar.gz wic.gz wic.bmap"
IMAGE_FEATURES += "allow-root-login"
IMAGE_LINGUAS = ""

EXTRA_USERS_PARAMS += "usermod -P root root;"

WKS_FILE ?= "${MACHINE}.wks"

# Packages to install
IMAGE_INSTALL = " \
    ${MACHINE_EXTRA_RDEPENDS} \
    kernel-image \
    packagegroup-core-boot \
    systemd-config \
"

IMAGE_PREPROCESS_COMMAND += "do_image_folders;"
do_image_folders() {
    install -d ${IMAGE_ROOTFS}/rpi
}
