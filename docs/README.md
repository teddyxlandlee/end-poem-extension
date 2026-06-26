# End Poem Extension

Briefly, this is a mod that enables loading alternative, multilingual
versions of End Poem.

In addition, the mod enables you to customize your End Poem, inserting text
before/after the End Poem, and append your mod / resource pack credits after
Mojang.

We have provided a **recommended resource pack** that includes several custom splashes, as well as random translations of the Poem in a few languages. You can either:
- **Install [Remote Resource Pack](https://modrinth.com/mod/remote-resource-pack) Mod** (so that the contents will be **auto-updated**), or
- [Manually download](https://epx-packs.mods.hixland.com/v2/dl/) the pack (and you may re-download it after a few days to see what's new or different)

Currently, Simplified Chinese (`zh_cn`), Cantonese (`zh_hk`), Traditional Chinese (`zh_tw`) and Literal Chinese (`lzh`) versions of End Poem are available.

[Contribute translations](https://github.com/Featurehouse/packs.epx.featurehouse.org) in your own language!

### Customizable files

Here's a list of files that you can customize:

+ **Localized End Poem**: `assets/end_poem_extension/texts/end_poem/xx_xx.txt` where
  `xx_xx` is replaced with language code.
  + Format: same as vanilla _End Poem_ in `assets/minecraft/texts/end.txt`.

+ **Localized PostCredits**: `assets/end_poem_extension/texts/postcredits/xx_xx.txt` where
  `xx_xx` is replaced with language code.
  + Format: same as above.

+ **Mod / Resource Pack Credits**: `assets/*/texts/mod_credits.json` where `*`
  can be any namespace you like.
  + Format: same as Mojang credits in `assets/minecraft/texts/credits.json`.

+ **Splash texts**: `assets/minecraft/texts/splash_modify.json`.
  + Format:
```json
{
  "add": [
    "Thanks Julian!",
    "Never Gonna Give You Up!",
    "You're Rickroll'd!",
    "..."
  ],
  "remove": [
    "...!",
    "Scary!"
  ]
}
```

+ Text before/after the *End Poem*: `assets/end_poem_extension/poem_pre.json`,
  `assets/end_poem_extension/poem_post.json`.
  + Format: just list the files. The following example indicates loaded files are:
    + `assets/example/path/to/file1.txt`
    + `assets/example/path/to/file2.txt`
    + `assets/example/path/to/dir3/xx_xx.txt` where `xx_xx` is current language code,
      and `default_suffix` property can be omitted (defaulting to `txt`).
  + The index files will __not__ be overwritten by other resource packs.
  + This format will be used in the following JSON index files.
```json
[
  "example:path/to/file1.txt",
  "example:path/to/file2.txt",
  {
    "is_i18n": true,
    "path": "example:path/to/dir3",
    "default_suffix": "txt"
  }
]
```

+ Inserting texts before/after `postcredits.txt`: `assets/end_poem_extension/pre_postcredits.json`,
  `assets/end_poem_extension/post_postcredits.json`
  + Format: See above

+ Inserting credits before Mojang (*not recommended*): `assets/end_poem_extension/pre_mojang_credits.json`
  + Format: See above (default value of `default_suffix` is here `json` instead)

Enjoy Mining!

### For resource pack creators: Filtering overwrites
When creating your resource pack, you may have directly overwritten vanilla `end.txt` or `credits.json` to
add what you want.

It's awful for cross-pack compatibility; with End Poem Extension installed, a better action is to create
`poem_post.json` or `poem_pre.json` (index files) to insert text after/before the poem, or
`mod_credits.json` to insert your credits after Mojang's.

However, to make sure your content available without End Poem Extension, you have to overwrite them on
vanilla paths, but you may expect these overwrites to be ignored by End Poem Extension, to ensure more
elegant compatibility.

To make your `end.txt` or `credits.json` skipped by End Poem Extension, create an `end.txt.mcmeta`
(or `credits.json.mcmeta`, correspondingly) in the same folder, with following content:
```json
{
  "end-poem-extension:skip": {}
}
```

Please note that localized resources of Poem (`assets/end_poem_extension/texts/end_poem/xx_xx.txt`)
are **not** skippable.