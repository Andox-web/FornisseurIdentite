import DashboardLayout from '../layout/DashboardLayout.vue'
import DashboardLayoutAdmin from '../layout/DashboardLayoutAdmin.vue'
// GeneralViews
import NotFound from '../pages/frontoffice/NotFoundPage.vue'

// Admin pages
import Overview from 'src/pages/frontoffice/CoursCrypto.vue'
import UserProfile from 'src/pages/frontoffice/UserProfile.vue'
import Annonce from 'src/pages/frontoffice/Annonce.vue'
import Portefeuille from 'src/pages/frontoffice/Portefeuille.vue'
import Upgrade from 'src/pages/frontoffice/Upgrade.vue'
import Userauth from 'src/pages/UserAuth.vue'
import Historic from 'src/pages/frontoffice/Historic.vue'
import Demande from 'src/pages/backoffice/Demandes.vue'
import Analyse from 'src/pages/frontoffice/Analyse.vue'
import Commission from 'src/pages/backoffice/Commission.vue'
import Commissionanalyse from 'src/pages/backoffice/AnalyseCommission.vue'
import Wallets from 'src/pages/backoffice/Wallets.vue'
import MyHistoric from 'src/pages/frontoffice/MyHistoric.vue'


const routes = [
  {
    path: '/',
    component: DashboardLayout,
    redirect: '/admin/'
  },
  {
    path: '/user',
    component: DashboardLayout,
    redirect: '/user/overview',
    children: [
      {
        path: 'overview',
        name: 'Overview',
        component: Overview
      },
      {
        path: 'user',
        name: 'User',
        component: UserProfile
      },
      {
        path: 'annonce',
        name: 'Annonce',
        component: Annonce
      },
      {
        path: 'portefeuille',
        name: 'Portefeuille',
        component: Portefeuille
      },
      {
        path: 'analyse',
        name: 'Analyse',
        component: Analyse
      },
      {
        path: 'historic',
        name: 'Historic',
        component: Historic
      },
      {
        path: 'myhistoric',
        name: 'Myhistoric',
        component: MyHistoric
      },
      {
        path: 'userauth',
        name: 'UserAuth',
        component: Userauth
      },
      {
        path: 'upgrade',
        name: 'Upgrade to PRO',
        component: Upgrade
      }
    ]
  },
  {
    path: '/admin',
    component: DashboardLayoutAdmin,
    redirect: '/admin/demande',
    children: [
      {
        path: 'demande',
        name: 'Demande',
        component: Demande
      },
      {
        path: 'commission',
        name: 'Commission',
        component: Commission
      },
      {
        path: 'commissionanalyse',
        name: 'Commissionanalyse',
        component: Commissionanalyse
      },
      {
        path: 'wallets',
        name: 'Wallets',
        component: Wallets
      },
      {
        path: 'analyse',
        name: 'Analyse',
        component: Analyse
      }
    ]
  },
  { path: '*', component: NotFound }
]

/**
 * Asynchronously load view (Webpack Lazy loading compatible)
 * The specified component must be inside the Views folder
 * @param  {string} name  the filename (basename) of the view to load.
function view(name) {
   var res= require('../components/Dashboard/Views/' + name + '.vue');
   return res;
};**/

export default routes
