(ns slipstream.ui.views.messages)

(def help
  {:help-module-is-base "This image maps to an already created image (possibly manually), which SlipStream didn't create. For base images, you require to provide cloud image identifiers for each cloud you want to use. For non-base images, you need to provide a reference image, which can be chained, to a base image."
   :help-reference-module "The image module this image inherits from."
   :help-cloud-image-ids "Contains the cloud specific image unique identifier (e.g. ami-xxxxxx for Amazon EC2)."
   :help-module-platform "The platform is defined by the base image and inherited by all derived images."
   :help-module-login "The login is defined by the base image and inherited by all derived images."})
  