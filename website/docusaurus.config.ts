import {themes as prismThemes} from 'prism-react-renderer';
import type {Config} from '@docusaurus/types';
import type * as Preset from '@docusaurus/preset-classic';

const config: Config = {
  title: 'Binance4s',
  tagline: 'Typeclass-driven, functional Scala 3 library for the Binance API',
  favicon: 'img/favicon.svg',

  future: {
    v4: true,
  },

  url: 'https://binance4s.rafaelfernandez.dev',
  baseUrl: '/',

  organizationName: 'eff3ct0',
  projectName: 'binance4s',

  onBrokenLinks: 'throw',

  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  stylesheets: [
    {
      href: 'https://fonts.googleapis.com/css2?family=Inter:wght@400;450;500;600;700&display=swap',
      type: 'text/css',
    },
  ],

  presets: [
    [
      'classic',
      {
        docs: {
          routeBasePath: '/',
          sidebarPath: './sidebars.ts',
          editUrl: 'https://github.com/eff3ct0/binance4s/edit/master/website/',
        },
        blog: false,
        theme: {
          customCss: './src/css/custom.css',
        },
      } satisfies Preset.Options,
    ],
  ],

  themeConfig: {
    image: 'img/binance4s-logo.svg',
    metadata: [
      { name: 'twitter:card', content: 'summary_large_image' },
    ],
    colorMode: {
      defaultMode: 'light',
      disableSwitch: false,
      respectPrefersColorScheme: true,
    },
    navbar: {
      title: 'Binance4s',
      logo: {
        alt: 'Binance4s',
        src: 'img/binance4s-logo.svg',
        srcDark: 'img/binance4s-logo-dark.svg',
        height: 22,
      },
      items: [
        {
          type: 'docSidebar',
          sidebarId: 'docsSidebar',
          position: 'left',
          label: 'Docs',
        },
        {
          href: 'https://developers.binance.com/docs/binance-spot-api-docs/rest-api',
          label: 'Binance API',
          position: 'left',
        },
        {
          href: 'https://github.com/eff3ct0/binance4s',
          label: 'GitHub',
          position: 'right',
        },
      ],
    },
    footer: {
      style: 'light',
      links: [
        {
          title: 'Docs',
          items: [
            {label: 'Introduction', to: '/'},
            {label: 'Getting Started', to: '/getting-started'},
            {label: 'API Reference', to: '/rest-api/general'},
          ],
        },
        {
          title: 'Binance API Reference',
          items: [
            {label: 'REST API', href: 'https://developers.binance.com/docs/binance-spot-api-docs/rest-api'},
            {label: 'WebSocket Streams', href: 'https://developers.binance.com/docs/binance-spot-api-docs/web-socket-streams'},
            {label: 'WebSocket API', href: 'https://developers.binance.com/docs/binance-spot-api-docs/websocket-api'},
          ],
        },
        {
          title: 'More',
          items: [
            {label: 'GitHub', href: 'https://github.com/eff3ct0/binance4s'},
            {label: 'Rafael Fernandez', href: 'https://rafaelfernandez.dev'},
          ],
        },
      ],
      copyright: `Copyright \u00A9 ${new Date().getFullYear()} Rafael Fern\u00E1ndez. Apache 2.0 License.`,
    },
    prism: {
      theme: prismThemes.github,
      darkTheme: prismThemes.vsDark,
      additionalLanguages: ['java', 'bash', 'scala'],
    },
  } satisfies Preset.ThemeConfig,
};

export default config;
