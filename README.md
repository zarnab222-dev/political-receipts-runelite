# Receipts for RuneLite

Your grind. Their filings. A quiet sidebar pairs a loot drop with a sourced US campaign finance receipt.

The clean player card shows one amount: contributions from other political committees. It uses a neutral background, readable type, a monogram, party, state, and chamber. Muted red amounts indicate a positive amount, green indicates zero in that category, and gray indicates unavailable data. These colors are not integrity ratings. Reporting dates remain visible. Open **What this means** for the exact amount, definitions, snapshot date, and session loot. All card artwork is original.

By **shin86dev**. Version 0.1.0. Public source preview. Not submitted to the Plugin Hub. Launch is deferred until the maintainer completes live game testing and chooses to submit it.

This repository contains the RuneLite companion to [Receipts for Chrome](https://github.com/zarnab222-dev/political-receipts). Publishing the source does not install or launch either product.

## Behavior

- Enable Loot Tracker. Receipts listens to its `LootReceived` event, including supported raid reward events.
- Open the barcode icon in the sidebar.
- Loot worth at least 50,000 GP produces a receipt. Change the minimum in plugin settings.
- Cards update at most once per minute by default. Small drops still add to the local session total.
- Campaign records rotate in roster order. No party, politician, or opinion targets the player.
- The sidebar does not send chat messages, show desktop notifications, automate gameplay, or make network requests.
- The FEC button opens the candidate's source page in your browser after you click it.
- Session loot resets when the plugin restarts. Loot is a GE estimate, not net profit. Unsold loot can change in value.

The two figures use different units and time periods. GP has no USD conversion here. Campaign receipts are not personal income, corporate donations, or evidence that a contribution bought a vote. Reported totals can include transfers between authorized committees more than once.

## Run locally

Use Java 11 or newer. Compile targets Java 11, as required by RuneLite.

```powershell
.\gradlew.bat test
.\gradlew.bat run
```

Use the [official Jagex account development-client instructions](https://github.com/runelite/runelite/wiki/Using-Jagex-Accounts) when your account requires them. Do not enter credentials into this repository or a script.

Before submission, test a real NPC drop and a Tombs of Amascut reward with Loot Tracker enabled. Confirm one receipt per accepted event, correct GE value, cooldown, reopening the sidebar, disabling the plugin, and an empty or unavailable source state. Live game testing is still required.

## Data and privacy

`src/main/resources/politicians.json` is a bundled snapshot. Its metadata includes retrieval time, source URLs, source hashes, coverage limits, and record counts. Each receipt shows that record's reporting period and an FEC link. Updates require a new plugin build. No analytics or browsing history are collected. Game events stay in memory.

The source is the official [FEC all-candidates bulk data](https://www.fec.gov/campaign-finance-data/all-candidates-file-description/) and the CC0 [Congress legislators roster](https://github.com/unitedstates/congress-legislators). This project uses public facts. It does not copy Integrity Index branding, grades, images, or proprietary summaries. The dataset is shared with the Chrome companion's reproducible data pipeline.

## Publishing after review

1. Vet the behavior, wording, visual design, and live game results.
2. Use the public source repository `zarnab222-dev/political-receipts-runelite`. `author=shin86dev` is display metadata; the GitHub owner is `zarnab222-dev`.
3. Push the exact reviewed commit. Keep the BSD 2-Clause license.
4. Fork `runelite/plugin-hub`. Add `plugins/political-receipts` with `repository=<public clone URL>` and `commit=<full reviewed SHA>`.
5. Submit one Plugin Hub pull request. Wait for CI and maintainer review. A local build is not approval or publication.

See the [official submission guide](https://github.com/runelite/plugin-hub#submitting-a-plugin). This project uses the standard build type and no additional runtime libraries. The Gradle wrapper comes from the official example plugin and checks the Gradle distribution SHA-256. Plugin Hub can replace the build file for a standard build.

## Implementation sources

- [Official example plugin](https://github.com/runelite/example-plugin)
- [LootReceived event source](https://github.com/runelite/runelite/blob/master/runelite-client/src/main/java/net/runelite/client/plugins/loottracker/LootReceived.java)
- [RuneLite developer guide](https://github.com/runelite/runelite/wiki/Developer-Guide)

Plugin source is BSD-2-Clause. Gradle wrapper files retain their upstream Apache-2.0 headers.
