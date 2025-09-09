# GitHub Events – Android Sample

Android application written in **Kotlin** and **Jetpack Compose** that lists public GitHub events and shows the full details for a selected event.

The project is structured as a multi-module Gradle setup and demonstrates clean, testable architecture with modern Android libraries and best practices.

---

## ✨ Features

- **Event feed**
  - Displays the five most relevant GitHub event types: **Push, PullRequest, Issues, Fork, Release**.
  - Uses **Paging 3** to stream results and keep a small memory footprint.
  - New events are fetched every **10 seconds** through a polling worker that respects GitHub **ETag** caching.

- **Event detail**
  - Tapping an item opens a full-screen detail page with the actor’s profile, repository info, time and date.
  - Quick links open the profile or repository in the browser.

- **Offline cache**
  - Events are stored locally using **Room** and served instantly on next launch.

- **Material-styled UI**
  - Fully composed screens with light/dark themes, placeholders, error states and preview annotations.

- **Test coverage**
  - Unit tests for interactors, repositories, data sources and view models.
  - Compose UI tests for critical widgets.
  - Instrumentation example under `libraries/design`.

---

## 🏗 Project Structure

```

app/                       Application entry point & navigation
features/
├─ search/                Event list screen (UI + view model)
├─ search-api/            Contracts & use cases for search feature
├─ event-detail/          Event detail screen (UI + view model)
└─ event-detail-api/      Contracts & use cases for detail feature
domain/                    Business models, ports and interactors
data/
├─ remote/                Retrofit + OkHttp + ETag handling
├─ local/                 Room database & DAOs
├─ repository/            Repository implementation, poller & mappers
libraries/
├─ design/                Compose theme, UI utilities, custom widgets
├─ navigation/            Navigation helpers and destination constants
└─ test/                  Testing DSL & robots
build-logic/               Gradle convention plugins for the modules

````

> The `*-api` modules expose only stable contracts so UI features depend on abstractions, keeping the architecture aligned with **Hexagonal + MVI** ideas.

---

## 🛠 Technical Stack

| Area     | Implementation                                    |
|----------|---------------------------------------------------|
| **UI**  | Jetpack Compose, Material Components              |
| **State** | Kotlin Flows, Coroutines, simple MVI-style states |
| **DI**  | Dagger-Hilt                                       |
| **Data** | Retrofit, OkHttp interceptors, Gson               |
| **Cache** | Room, Paging 3, in-memory ETag store             |
| **Polling** | Custom EventsPoller using CoroutineScope       |
| **Testing** | JUnit5, MockWebServer, Compose test APIs, robots |
| **Build** | Gradle Kotlin DSL, custom convention plugins     |

---

## 🎨 Design & UX Notes

* Composables are fully **themable**; colors and typography live under `libraries/design`.
* UI makes extensive use of **previews** for isolated iteration.
* **Snackbars** and retry buttons provide quick feedback for network errors.

---