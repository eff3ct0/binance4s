import type {SidebarsConfig} from '@docusaurus/plugin-content-docs';

const sidebars: SidebarsConfig = {
  docsSidebar: [
    'intro',
    'getting-started',
    {
      type: 'category',
      label: 'Concepts',
      items: [
        'concepts/typeclass-architecture',
        'concepts/resource-safety',
        'concepts/error-handling',
      ],
    },
    {
      type: 'category',
      label: 'REST API',
      items: [
        'rest-api/general',
        'rest-api/market-data',
        'rest-api/spot-trading',
        'rest-api/account',
        'rest-api/wallet',
        'rest-api/user-data-stream',
      ],
    },
    {
      type: 'category',
      label: 'WebSocket',
      items: [
        'websocket/streams',
        'websocket/user-data',
      ],
    },
    'configuration',
    {
      type: 'category',
      label: 'Reference',
      items: [
        'reference/api-coverage',
        'reference/binance-api-links',
      ],
    },
  ],
};

export default sidebars;
