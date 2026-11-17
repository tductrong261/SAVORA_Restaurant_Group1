const SavoraTheme = (() => {
    const root = document.documentElement;
    const storageKey = 'savora-theme';

    const getSavedTheme = () => {
        try {
            const saved = localStorage.getItem(storageKey);
            if (saved === 'dark' || saved === 'light') {
                return saved;
            }
        } catch {
            // Storage unavailable
        }
        return 'light';
    };

    const applyTheme = (theme) => {
        root.dataset.theme = theme === 'dark' ? 'dark' : 'light';
    };

    applyTheme(getSavedTheme());

    const api = {
        bind(button) {
            if (!button) return;

            const updateButton = () => {
                const isDark = root.dataset.theme === 'dark';
                button.setAttribute('aria-pressed', String(isDark));
                button.setAttribute('aria-label', isDark ? 'Light mode' : 'Dark mode');
            };

            updateButton();

            if (button.dataset.themeBound === 'true') {
                return;
            }
            button.dataset.themeBound = 'true';

            button.addEventListener('click', () => {
                const nextTheme = root.dataset.theme === 'dark' ? 'light' : 'dark';
                applyTheme(nextTheme);
                updateButton();

                try {
                    localStorage.setItem(storageKey, nextTheme);
                } catch {
                    // The current page can still switch themes when storage is unavailable.
                }
            });
        }
    };

    window.SavoraTheme = api;

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', () => {
            api.bind(document.getElementById('theme-toggle'));
        });
    } else {
        api.bind(document.getElementById('theme-toggle'));
    }

    return api;
})();
