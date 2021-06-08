const update_yml_version = (contents, previousVersion, releaseVersion) => {
  return contents.replace(
    /\nversion\:[\s]+[0-9]+(\.[0-9]+)+/g,
    `\nversion: ${releaseVersion}`
  );
};

const update_plugin_annotation = (contents, previousVersion, releaseVersion) => {
  return contents.replace(
    /version[\s]+=[\s]+"[0-9]+(\.[0-9]+)+"/g,
    `version = "${releaseVersion}"`
  );
};

const update_yml_quote_version = (contents, previousVersion, releaseVersion) => {
  return contents.replace(
    /\nversion\:[\s]+"[0-9]+(\.[0-9]+)+"/g,
    `\nversion: "${releaseVersion}"`
  );
};

const append_version = (contents, previousVersion, releaseVersion) => {
  return contents.replace(
    /ALLOWED_VERSIONS = new String\[\] {[\s]+/g,
    `ALLOWED_VERSIONS = new String[] {\n\t\t\t"${releaseVersion}",\n\t\t\t`
  );
};

const append_version_latest = (contents, previousVersion, releaseVersion) => {
  return contents.replace(
    /LATEST_VERSION = "[0-9]+(\.[0-9]+)+";/g,
    `LATEST_VERSION = "${releaseVersion}";`
  );
};

module.exports = {
  bumpFiles: [
    {
      path: 'src/main/java/xyz/olivermartin/multichat/local/sponge/MultiChatLocalSpongePlugin.java',
      task: update_plugin_annotation
    },
    {
      path: 'src/main/java/xyz/olivermartin/multichat/bungee/MultiChat.java',
      task: append_version
    },
    {
      path: 'src/main/java/xyz/olivermartin/multichat/bungee/MultiChat.java',
      task: append_version_latest
    },
    {
      path: 'src/main/resources/bungee.yml',
      task: update_yml_version
    },
    {
      path: 'src/main/resources/plugin.yml',
      task: update_yml_version
    },
    {
      path: 'src/main/resources/chatcontrol_fr.yml',
      task: update_yml_quote_version
    },
    {
      path: 'src/main/resources/chatcontrol.yml',
      task: update_yml_quote_version
    },
    {
      path: 'src/main/resources/config_fr.yml',
      task: update_yml_quote_version
    },
    {
      path: 'src/main/resources/config.yml',
      task: update_yml_quote_version
    },
    {
      path: 'src/main/resources/joinmessages_fr.yml',
      task: update_yml_quote_version
    },
    {
      path: 'src/main/resources/joinmessages.yml',
      task: update_yml_quote_version
    },
    {
      path: 'src/main/resources/localconfig_fr.yml',
      task: update_yml_quote_version
    },
    {
      path: 'src/main/resources/localconfig.yml',
      task: update_yml_quote_version
    },
    {
      path: 'src/main/resources/messages_fr.yml',
      task: update_yml_quote_version
    },
    {
      path: 'src/main/resources/messages.yml',
      task: update_yml_quote_version
    }
  ]
};
