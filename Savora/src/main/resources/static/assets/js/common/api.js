/**
 * SAVORA FRONTEND CORE UTILITY (assets/js/common/api.js)
 * Shared utility across all HTML pages to call Spring Boot REST APIs and initialize header & scroll interactions
 */
const SavoraApp = {
    getBaseUrl() {
        if (window.location.port === '5500' || window.location.port === '5501') {
            return 'http://localhost:8080';
        }
        return '';
    },

    async fetchApi(endpoint, options = {}) {
        const url = this.getBaseUrl() + endpoint;
        const defaultHeaders = {
            'Content-Type': 'application/json'
        };
        const config = {
            credentials: 'include',
            ...options,
            headers: {
                ...defaultHeaders,
                ...(options.headers || {})
            }
        };

        const response = await fetch(url, config);
        const result = await response.json();
        if (!response.ok || result.success === false) {
            throw new Error(result.message || 'An error occurred while connecting to the server.');
        }
        return result;
    },

    formatCurrency(amount) {
        if (amount == null) return '0 VND';
        return Number(amount).toLocaleString('en-US') + ' VND';
    },

    async loadLayout() {
        const toggle = document.getElementById('theme-toggle');
        if (toggle && window.SavoraTheme) {
            SavoraTheme.bind(toggle);
        }

        // Highlight active navigation link
        const currentPath = window.location.pathname;
        document.querySelectorAll('.header__nav-link[data-nav]').forEach(link => {
            const target = link.getAttribute('data-nav');
            if ((target === '/home' && (currentPath === '/' || currentPath === '/home'))
                || (target !== '/home' && currentPath.startsWith(target))) {
                link.classList.add('is-active');
            }
        });

        // User dropdown menu interaction
        const userMenu = document.getElementById('user-menu');
        const userTrigger = document.getElementById('user-menu-trigger');
        if (userMenu && userTrigger && !userTrigger.dataset.bound) {
            userTrigger.dataset.bound = 'true';
            userTrigger.addEventListener('click', (e) => {
                e.stopPropagation();
                const isOpen = userMenu.classList.toggle('is-open');
                userTrigger.setAttribute('aria-expanded', String(isOpen));
            });

            document.addEventListener('click', (e) => {
                if (!userMenu.contains(e.target)) {
                    userMenu.classList.remove('is-open');
                    userTrigger.setAttribute('aria-expanded', 'false');
                }
            });
        }

        // Mobile navigation toggle
        const mobileToggle = document.getElementById('mobile-nav-toggle');
        const mainNav = document.getElementById('main-nav');
        if (mobileToggle && mainNav && !mobileToggle.dataset.bound) {
            mobileToggle.dataset.bound = 'true';
            mobileToggle.addEventListener('click', () => {
                const isOpen = mainNav.classList.toggle('is-open');
                mobileToggle.setAttribute('aria-expanded', String(isOpen));
            });
        }

        // Sticky Header shadow & Back-to-Top button on scroll
        const headerEl = document.getElementById('site-header') || document.querySelector('.header');
        const backToTopBtn = document.getElementById('back-to-top');

        const handleScroll = () => {
            const scrollY = window.scrollY || document.documentElement.scrollTop;
            if (headerEl) {
                headerEl.classList.toggle('is-scrolled', scrollY > 36);
            }
            if (backToTopBtn) {
                backToTopBtn.classList.toggle('is-visible', scrollY > 260);
            }
        };

        if (!window.__savoraScrollBound) {
            window.__savoraScrollBound = true;
            window.addEventListener('scroll', handleScroll, { passive: true });
        }
        handleScroll();

        if (backToTopBtn && !backToTopBtn.dataset.bound) {
            backToTopBtn.dataset.bound = 'true';
            backToTopBtn.addEventListener('click', () => {
                window.scrollTo({
                    top: 0,
                    behavior: 'smooth'
                });
            });
        }
    },

    async syncAuthState() {
        // Thymeleaf renders the current session into the header fragment.
    }
};

document.addEventListener('DOMContentLoaded', () => {
    SavoraApp.loadLayout();
});
